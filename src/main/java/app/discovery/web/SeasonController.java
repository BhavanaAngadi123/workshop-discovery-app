package app.discovery.web;

import app.discovery.domain.Season;
import app.discovery.repo.SeasonRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seasons")
@Tag(name = "Seasons")
public class SeasonController {
  private final SeasonRepository seasons;

  public SeasonController(SeasonRepository seasons) {
    this.seasons = seasons;
  }

  @GetMapping
  @Operation(summary = "Active seasons — the re-engagement engine (autumn pumpkin picking, spring tours...)")
  public List<Map<String, Object>> active() {
    return seasons.findByActiveTrue().stream()
        .map(s -> Map.<String, Object>of(
            "slug", s.getSlug(), "name", s.getName(),
            "description", s.getDescription() == null ? "" : s.getDescription(),
            "startMonth", s.getStartMonth(), "endMonth", s.getEndMonth(),
            "bannerUrl", s.getBannerUrl() == null ? "" : s.getBannerUrl()))
        .toList();
  }

  @PostMapping
  @Operation(summary = "Admin: create a season")
  @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
  public Map<String, Object> create(@RequestBody Map<String, Object> body) {
    Season s = new Season();
    s.setSlug(String.valueOf(body.get("slug")));
    s.setName(String.valueOf(body.get("name")));
    s.setDescription((String) body.get("description"));
    s.setStartMonth(Integer.parseInt(String.valueOf(body.get("startMonth"))));
    s.setEndMonth(Integer.parseInt(String.valueOf(body.get("endMonth"))));
    s.setBannerUrl((String) body.get("bannerUrl"));
    seasons.save(s);
    return Map.of("slug", s.getSlug(), "name", s.getName());
  }
}
