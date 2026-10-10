package app.discovery.poster;

import app.discovery.poster.templates.BotanicalTemplate;
import app.discovery.poster.templates.DuskTemplate;
import app.discovery.poster.templates.InkTemplate;
import app.discovery.poster.templates.SunriseTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Server-side poster studio: picks a template, renders a 1080x1350 PNG,
 * stores it under the public posters directory. No external APIs.
 */
@Service
public class PosterRenderer {
  public static final int WIDTH = 1080;
  public static final int HEIGHT = 1350;

  private final Map<String, PosterTemplate> templates;
  private final FontSet fonts;
  private final Path posterDir;

  public PosterRenderer(@Value("${app.posters.dir:posters}") String posterDir) throws IOException {
    List<PosterTemplate> all = List.of(
        new SunriseTemplate(), new BotanicalTemplate(), new InkTemplate(), new DuskTemplate());
    this.templates = all.stream().collect(Collectors.toMap(PosterTemplate::id, Function.identity()));
    this.fonts = FontSet.load();
    this.posterDir = Path.of(posterDir);
    Files.createDirectories(this.posterDir);
  }

  public List<PosterTemplate> templates() {
    return List.copyOf(templates.values());
  }

  public PosterTemplate template(String id) {
    return templates.getOrDefault(id == null ? "" : id, templates.get("sunrise"));
  }

  /** Renders and stores the poster; returns the public URL path. */
  public String render(PosterSpec spec) throws IOException {
    PosterTemplate t = template(spec.templateId());
    BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = img.createGraphics();
    try {
      t.render(g, spec, WIDTH, HEIGHT, fonts);
    } finally {
      g.dispose();
    }
    String fileName = "poster-" + UUID.randomUUID() + ".png";
    Path out = posterDir.resolve(fileName);
    ImageIO.write(img, "png", out.toFile());
    return "/posters/" + fileName;
  }
}
