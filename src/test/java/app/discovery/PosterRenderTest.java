package app.discovery;

import app.discovery.poster.PosterRenderer;
import app.discovery.poster.PosterSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PosterRenderTest {
  @Autowired PosterRenderer posters;

  @Test
  void allTemplatesRender(@TempDir Path tmp) {
    assertEquals(4, posters.templates().size());
    for (var t : posters.templates()) {
      PosterSpec spec = new PosterSpec(
          "Sunset Pottery & Wine Night", "POTTERY", "Sat, Oct 18 · 6:00 PM",
          "$45 per person", "Hosted by Clay House", t.id(), 3);
      java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
          PosterRenderer.WIDTH, PosterRenderer.HEIGHT, java.awt.image.BufferedImage.TYPE_INT_RGB);
      var g = img.createGraphics();
      try {
        t.render(g, spec, PosterRenderer.WIDTH, PosterRenderer.HEIGHT,
            app.discovery.poster.FontSet.load());
      } finally {
        g.dispose();
      }
      // spot check: image is not blank
      int rgb = img.getRGB(PosterRenderer.WIDTH / 2, PosterRenderer.HEIGHT / 2);
      assertNotEquals(0, rgb);
    }
    assertTrue(Files.exists(tmp)); // temp dir sanity
  }
}
