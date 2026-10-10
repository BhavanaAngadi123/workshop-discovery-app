package app.discovery.service;

import app.discovery.domain.User;
import app.discovery.domain.Workshop;
import app.discovery.domain.WorkshopCategory;
import app.discovery.poster.PosterRenderer;
import app.discovery.poster.PosterSpec;
import app.discovery.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * AI Listing Studio: a host describes their workshop in plain words (or a tiny form);
 * the AI writes the listing copy, picks a poster template, and renders an eye-catching
 * publicity poster. The host can take it as-is, tweak, pick another template, or skip
 * AI entirely and post manually. Copy generation gracefully degrades to smart
 * templates when the local LLM is unreachable.
 */
@Service
public class ListingStudioService {
  private static final Logger log = LoggerFactory.getLogger(ListingStudioService.class);

  private final ObjectProvider<ChatClient.Builder> chatClientBuilder;
  private final PosterRenderer posters;
  private final WorkshopService workshopService;
  private final UserRepository users;
  private final AuditService audit;

  public ListingStudioService(ObjectProvider<ChatClient.Builder> chatClientBuilder,
                              PosterRenderer posters, WorkshopService workshopService,
                              UserRepository users, AuditService audit) {
    this.chatClientBuilder = chatClientBuilder;
    this.posters = posters;
    this.workshopService = workshopService;
    this.users = users;
    this.audit = audit;
  }

  public record DraftRequest(
      String ideaText, String categoryHint, String dateHint,
      Integer slots, Long feeCents, String includesHint,
      Integer durationMinutes, String city, String templateId) {}

  public record TemplateOption(String id, String name, String blurb) {}

  public record ListingDraft(
      String title, String description, List<String> includes,
      String templateId, List<TemplateOption> templateOptions,
      String posterPreviewUrl, Long suggestedPriceCents,
      Integer suggestedCapacity, String dateLine) {}

  /** Generate a full listing draft from rough host words. */
  public ListingDraft generateDraft(DraftRequest req) throws IOException {
    String idea = req.ideaText() == null ? "" : req.ideaText().trim();
    if (idea.isBlank()) throw new IllegalArgumentException("Tell us your workshop idea in a few words");

    AiCopy copy = aiCopy(req, idea);
    String templateId = req.templateId() != null && !req.templateId().isBlank()
        ? req.templateId() : copy.templateId();

    String dateLine = req.dateHint() != null && !req.dateHint().isBlank() ? req.dateHint() : "Dates coming soon";
    String priceLine = copy.priceCents() != null ? formatPrice(copy.priceCents()) : "";

    String posterUrl = posters.render(new PosterSpec(
        copy.title(), copy.kicker(), dateLine, priceLine, "", templateId,
        Math.abs(idea.hashCode()) % 997));

    List<TemplateOption> options = posters.templates().stream()
        .map(t -> new TemplateOption(t.id(), t.name(), t.blurb()))
        .toList();

    return new ListingDraft(copy.title(), copy.description(), copy.includes(),
        templateId, options, posterUrl, copy.priceCents(),
        req.slots() != null ? req.slots() : 12, dateLine);
  }

  /** Publish a draft (possibly host-edited) as a real workshop listing. */
  @Transactional
  public Workshop publish(Long hostId, ListingDraft draft, WorkshopCategory category,
                          String city, String locationName, List<SessionSpec> sessions) {
    User host = users.findById(hostId).orElseThrow(() -> new IllegalArgumentException("Host not found"));
    Workshop w = new Workshop();
    w.setTitle(draft.title());
    w.setDescription(draft.description());
    w.setCategory(category == null ? WorkshopCategory.OTHER : category);
    w.setPriceCents(draft.suggestedPriceCents() == null ? 0 : draft.suggestedPriceCents());
    w.setCity(city);
    w.setLocationName(locationName);
    w.setPosterUrl(draft.posterPreviewUrl());
    w.setCoverPhotoUrl(draft.posterPreviewUrl());
    w.setIncludes(draft.includes() == null ? "" : String.join("\n", draft.includes()));
    Workshop created = workshopService.createDraft(hostId, w);
    if (sessions != null) {
      for (SessionSpec s : sessions) {
        workshopService.addSession(hostId, created.getId(), s.startsAt(), s.endsAt(), s.capacity());
      }
    }
    workshopService.publish(hostId, created.getId());
    audit.log(hostId, "LISTING_AI_PUBLISH", "Workshop", created.getId());
    return created;
  }

  public record SessionSpec(Instant startsAt, Instant endsAt, int capacity) {}

  // ---------- AI copy generation ----------

  private record AiCopy(String title, String kicker, String description, List<String> includes,
                        String templateId, Long priceCents) {}

  private AiCopy aiCopy(DraftRequest req, String idea) {
    String prompt = """
        You are a marketing copywriter for a local workshops marketplace. A host described
        their workshop idea in rough words. Write an attractive listing.

        Host's words: "%s"
        Category hint: %s | Fee hint (cents): %s | What's included hint: %s | Duration: %s min | City: %s

        House rules (mandatory): every listing MUST show price, timings/duration, location,
        and what's-included — never write "contact for price". Keep the tone warm and specific.

        Respond with ONLY valid JSON (no markdown fences) with these keys:
        {
          "title": "catchy title, max 60 chars",
          "kicker": "short category label, 1-3 words, uppercase feel",
          "description": "2-3 warm sentences selling the experience, max 400 chars",
          "includes": ["3-6 short bullet strings of what's included"],
          "templateId": one of: sunrise, botanical, ink, dusk,
          "priceCents": suggested price in cents as a number, or null if unclear
        }
        """.formatted(idea, or(req.categoryHint(), "?"), or(req.feeCents(), "?"),
            or(req.includesHint(), "?"), or(req.durationMinutes(), "?"), or(req.city(), "?"));

    try {
      var builder = chatClientBuilder.getIfAvailable();
      if (builder != null) {
        String json = builder.build().prompt().user(prompt).call().content();
        return parseAiJson(json, req);
      }
    } catch (Exception e) {
      log.info("Ollama unavailable for listing copy ({}); using smart templates", e.getMessage());
    }
    return templateCopy(req, idea);
  }

  private AiCopy parseAiJson(String json, DraftRequest req) {
    try {
      String clean = json.trim();
      if (clean.startsWith("```")) clean = clean.replaceAll("^```[a-zA-Z]*", "").replaceAll("```$", "").trim();
      var node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(clean);
      String title = node.path("title").asText("Untitled Workshop");
      String kicker = node.path("kicker").asText("WORKSHOP");
      String description = node.path("description").asText("");
      List<String> includes = new java.util.ArrayList<>();
      node.path("includes").forEach(n -> includes.add(n.asText()));
      String templateId = node.path("templateId").asText("sunrise");
      Long priceCents = node.path("priceCents").isNumber() ? node.path("priceCents").asLong() : null;
      if (req.feeCents() != null) priceCents = req.feeCents();
      return new AiCopy(title, kicker, description,
          includes.isEmpty() ? List.of("All materials included", "Guided by your host") : includes,
          templateId, priceCents);
    } catch (Exception e) {
      log.info("Could not parse AI listing JSON; using smart templates");
      return templateCopy(req, "");
    }
  }

  /** Smart non-AI fallback: good copy from the host's own words. Never fails. */
  private AiCopy templateCopy(DraftRequest req, String idea) {
    String cat = req.categoryHint() != null && !req.categoryHint().isBlank()
        ? req.categoryHint().trim() : "Hands-on Workshop";
    String title = toTitleCase(idea.length() > 60 ? idea.substring(0, 60) : idea);
    if (title.isBlank()) title = "A Lovely " + cat;
    String description = "Join us for " + idea.toLowerCase() + ". "
        + "A relaxed, beginner-friendly session where you'll learn by doing, meet lovely people, "
        + "and leave with something you made yourself."
        .replace("..", ".");
    List<String> includes = new java.util.ArrayList<>();
    if (req.includesHint() != null && !req.includesHint().isBlank()) {
      for (String part : req.includesHint().split("[,;]")) {
        if (!part.isBlank()) includes.add(toTitleCase(part.trim()));
      }
    }
    if (includes.isEmpty()) includes.addAll(List.of("All materials included", "Beginner friendly", "Guided by your host"));
    String templateId = pickTemplateFor(cat);
    return new AiCopy(title, cat.toUpperCase(), description, includes, templateId, req.feeCents());
  }

  private String pickTemplateFor(String category) {
    String c = category.toLowerCase();
    if (c.contains("garden") || c.contains("plant") || c.contains("pumpkin") || c.contains("farm")) return "botanical";
    if (c.contains("pottery") || c.contains("clay") || c.contains("morning") || c.contains("pasta")) return "sunrise";
    if (c.contains("evening") || c.contains("night") || c.contains("wine") || c.contains("candle")) return "dusk";
    return "ink";
  }

  private static String or(Object v, String dflt) {
    return v == null ? dflt : String.valueOf(v);
  }

  private static String toTitleCase(String s) {
    if (s == null || s.isBlank()) return "";
    String[] words = s.trim().split("\\s+");
    StringBuilder sb = new StringBuilder();
    for (String w : words) {
      if (!w.isEmpty()) sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
    }
    return sb.toString().trim();
  }

  private static String formatPrice(long cents) {
    return "$" + (cents / 100) + (cents % 100 == 0 ? "" : "." + String.format("%02d", cents % 100));
  }
}
