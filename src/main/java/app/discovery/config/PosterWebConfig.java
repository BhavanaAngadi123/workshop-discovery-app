package app.discovery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

/** Serves AI-generated posters as static files. */
@Configuration
public class PosterWebConfig implements WebMvcConfigurer {
  private final String posterDir;

  public PosterWebConfig(@Value("${app.posters.dir:posters}") String posterDir) {
    this.posterDir = posterDir;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String location = Path.of(posterDir).toAbsolutePath().toUri().toString();
    registry.addResourceHandler("/posters/**").addResourceLocations(location);
  }
}
