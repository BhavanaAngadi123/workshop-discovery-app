package app.discovery.service;

import app.discovery.domain.Workshop;
import app.discovery.domain.WorkshopCategory;
import app.discovery.domain.WorkshopStatus;
import app.discovery.repo.WorkshopRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * The curation brain: scores published workshops for a seeker.
 * Used directly by REST and exposed as tools to the AI concierge.
 */
@Service
public class RecommendationService {
  private final WorkshopRepository workshops;

  public RecommendationService(WorkshopRepository workshops) {
    this.workshops = workshops;
  }

  public record ScoredWorkshop(Workshop workshop, double score, String why) {}

  /**
   * Curate workshops for free-form intent: optional category keywords, max budget,
   * city. Score = rating weight + budget fit + popularity + recency nudge.
   */
  public List<ScoredWorkshop> recommend(String categoryHint, Long maxPriceCents, String city, int limit) {
    WorkshopCategory category = parseCategory(categoryHint);
    var page = workshops.search(category, city, null, maxPriceCents, null, PageRequest.of(0, Math.max(limit * 4, 20)));
    return page.getContent().stream()
        .map(w -> new ScoredWorkshop(w, score(w, maxPriceCents), why(w, maxPriceCents)))
        .sorted(Comparator.comparingDouble(ScoredWorkshop::score).reversed())
        .limit(limit)
        .toList();
  }

  private double score(Workshop w, Long maxPriceCents) {
    double s = 0;
    s += Math.min(w.getRatingAvg(), 5.0) * 12;                    // ratings matter most
    s += Math.min(w.getRatingCount(), 50);                        // social proof
    if (maxPriceCents != null && w.getPriceCents() <= maxPriceCents) s += 25; // budget fit
    else if (maxPriceCents == null) s += 10;
    if (w.getCoverPhotoUrl() != null || w.getPosterUrl() != null) s += 8;     // visual listings win
    return s;
  }

  private String why(Workshop w, Long maxPriceCents) {
    StringBuilder sb = new StringBuilder();
    if (w.getRatingCount() > 0) sb.append(String.format("%.1f★ from %d reviews", w.getRatingAvg(), w.getRatingCount()));
    else sb.append("New listing");
    if (maxPriceCents != null && w.getPriceCents() <= maxPriceCents) sb.append(" · fits your budget");
    if (w.getCity() != null) sb.append(" · ").append(w.getCity());
    return sb.toString();
  }

  static WorkshopCategory parseCategory(String hint) {
    if (hint == null || hint.isBlank()) return null;
    String h = hint.toLowerCase();
    for (WorkshopCategory c : WorkshopCategory.values()) {
      if (h.contains(c.name().toLowerCase().replace('_', ' ')) || h.contains(c.name().toLowerCase()))
        return c;
    }
    if (h.contains("pottery") || h.contains("clay") || h.contains("ceramic")) return WorkshopCategory.POTTERY;
    if (h.contains("pasta") || h.contains("cook") || h.contains("croissant")) return WorkshopCategory.COOKING;
    if (h.contains("cake") || h.contains("pastry") || h.contains("chocolate")) return WorkshopCategory.BAKING;
    if (h.contains("garden tour") || h.contains("farm")) return WorkshopCategory.GARDEN_TOUR;
    if (h.contains("garden") || h.contains("plant")) return WorkshopCategory.GARDENING;
    if (h.contains("pumpkin") || h.contains("autumn") || h.contains("fall ") || h.contains("seasonal"))
      return WorkshopCategory.SEASONAL;
    if (h.contains("knit")) return WorkshopCategory.KNITTING;
    if (h.contains("draw") || h.contains("paint") || h.contains("art")) return WorkshopCategory.ART;
    if (h.contains("date")) return null; // "pottery date" -> keep broad, keyword match handles it
    return null;
  }
}
