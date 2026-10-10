package app.discovery.poster;

import java.awt.Graphics2D;

/** One poster art direction. Implementations draw the full 1080x1350 canvas. */
public interface PosterTemplate {
  String id();
  String name();
  String blurb();
  void render(Graphics2D g, PosterSpec spec, int w, int h, FontSet fonts);
}
