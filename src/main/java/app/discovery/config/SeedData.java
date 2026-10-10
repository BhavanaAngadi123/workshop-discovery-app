package app.discovery.config;

import app.discovery.domain.Season;
import app.discovery.repo.SeasonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Seeds the seasonal calendar — the re-engagement engine. */
@Configuration
public class SeedData {

  @Bean
  CommandLineRunner seedSeasons(SeasonRepository seasons) {
    return args -> {
      seed(seasons, "autumn", "Autumn Harvest",
          "Pumpkin picking, apple orchards, fall farm days and cozy harvest workshops.", 9, 11);
      seed(seasons, "winter", "Winter Magic",
          "Candle making, wreath workshops, hot chocolate sessions and holiday crafts.", 12, 2);
      seed(seasons, "spring", "Spring Bloom",
          "Garden tours, flower arranging, planting workshops and farm visits.", 3, 5);
      seed(seasons, "summer", "Summer Days",
          "Outdoor pottery, plein-air painting, garden parties and farm-to-table cooking.", 6, 8);
    };
  }

  private void seed(SeasonRepository seasons, String slug, String name, String desc, int start, int end) {
    if (seasons.findBySlug(slug).isEmpty()) {
      Season s = new Season();
      s.setSlug(slug);
      s.setName(name);
      s.setDescription(desc);
      s.setStartMonth(start);
      s.setEndMonth(end);
      seasons.save(s);
    }
  }
}
