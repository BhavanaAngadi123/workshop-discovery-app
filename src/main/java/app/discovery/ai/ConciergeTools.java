package app.discovery.ai;

import app.discovery.domain.Workshop;
import app.discovery.repo.SeasonRepository;
import app.discovery.repo.WorkshopRepository;
import app.discovery.service.RecommendationService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tools the AI concierge calls: real workshop data, never hallucinated.
 * The LLM handles conversation; these tools ground every recommendation.
 */
@Component
public class ConciergeTools {
  private final RecommendationService recommendations;
  private final WorkshopRepository workshops;
  private final SeasonRepository seasons;

  public ConciergeTools(RecommendationService recommendations,
                        WorkshopRepository workshops, SeasonRepository seasons) {
    this.recommendations = recommendations;
    this.workshops = workshops;
    this.seasons = seasons;
  }

  @Tool(description = "Search published workshops. Use for any 'find me a workshop' request. "
      + "categoryHint: free text like 'pottery', 'pasta making', 'pumpkin picking'. "
      + "maxPriceCents: budget cap in cents, e.g. 5000 = $50. city: optional city name.")
  public List<Map<String, Object>> searchWorkshops(
      @ToolParam(description = "free-text category/activity hint") String categoryHint,
      @ToolParam(description = "max budget in cents, null if none") Long maxPriceCents,
      @ToolParam(description = "city, null for anywhere") String city) {
    return recommendations.recommend(categoryHint, maxPriceCents, city, 6).stream()
        .map(s -> {
          Workshop w = s.workshop();
          return Map.<String, Object>of(
              "id", w.getId(),
              "title", w.getTitle(),
              "category", w.getCategory().name(),
              "price", "$" + w.getPriceCents() / 100,
              "city", w.getCity() == null ? "" : w.getCity(),
              "rating", w.getRatingAvg() + " (" + w.getRatingCount() + " reviews)",
              "why", s.why());
        })
        .collect(Collectors.toList());
  }

  @Tool(description = "List workshops for a seasonal theme, e.g. slug 'autumn' for pumpkin picking, "
      + "'spring' for garden tours. Use when the user mentions a season or holiday.")
  public List<Map<String, Object>> seasonalWorkshops(
      @ToolParam(description = "season slug like 'autumn', 'spring', 'summer', 'winter'") String seasonSlug) {
    return workshops.search(null, null, null, null, seasonSlug,
            org.springframework.data.domain.PageRequest.of(0, 8))
        .getContent().stream()
        .map(w -> Map.<String, Object>of(
            "id", w.getId(), "title", w.getTitle(),
            "price", "$" + w.getPriceCents() / 100,
            "city", w.getCity() == null ? "" : w.getCity(),
            "rating", w.getRatingAvg() + " (" + w.getRatingCount() + " reviews)"))
        .collect(Collectors.toList());
  }

  @Tool(description = "Get full details of one workshop by id, including upcoming sessions.")
  public Map<String, Object> workshopDetails(
      @ToolParam(description = "workshop id") Long workshopId) {
    return workshops.findById(workshopId).map(w -> Map.<String, Object>of(
        "id", w.getId(),
        "title", w.getTitle(),
        "description", w.getDescription() == null ? "" : w.getDescription(),
        "category", w.getCategory().name(),
        "price", "$" + w.getPriceCents() / 100,
        "location", (w.getLocationName() == null ? "" : w.getLocationName() + ", ")
            + (w.getCity() == null ? "" : w.getCity()),
        "rating", w.getRatingAvg() + " (" + w.getRatingCount() + " reviews)"))
        .orElse(Map.of("error", "workshop not found"));
  }

  @Tool(description = "List currently active seasons (autumn, spring, ...).")
  public List<Map<String, Object>> activeSeasons() {
    return seasons.findByActiveTrue().stream()
        .map(s -> Map.<String, Object>of("slug", s.getSlug(), "name", s.getName(),
            "description", s.getDescription() == null ? "" : s.getDescription()))
        .collect(Collectors.toList());
  }
}
