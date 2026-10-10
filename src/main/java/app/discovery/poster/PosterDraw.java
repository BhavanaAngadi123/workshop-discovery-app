package app.discovery.poster;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Shared Java2D helpers for poster templates. */
public final class PosterDraw {
  private PosterDraw() {}

  public static void prep(Graphics2D g) {
    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
        java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,
        java.awt.RenderingHints.VALUE_RENDER_QUALITY);
  }

  /** Letter-spaced uppercase kicker, centered. */
  public static void kicker(Graphics2D g, String text, Font font, Color color, int cx, int y) {
    if (text == null || text.isBlank()) return;
    String spaced = text.toUpperCase().chars()
        .mapToObj(c -> String.valueOf((char) c))
        .reduce("", (a, b) -> a.isEmpty() ? b : a + "\u2009" + b);
    g.setFont(font);
    g.setColor(color);
    FontMetrics fm = g.getFontMetrics();
    g.drawString(spaced, cx - fm.stringWidth(spaced) / 2, y);
  }

  /** Word-wrapped centered paragraph. Returns y after last line. */
  public static int wrappedCenter(Graphics2D g, String text, Font font, Color color,
                                  int cx, int y, int maxWidth, int lineHeight) {
    if (text == null || text.isBlank()) return y;
    g.setFont(font);
    g.setColor(color);
    FontMetrics fm = g.getFontMetrics();
    List<String> lines = new ArrayList<>();
    for (String para : text.split("\n")) {
      StringBuilder line = new StringBuilder();
      for (String word : para.split("\\s+")) {
        String trial = line.isEmpty() ? word : line + " " + word;
        if (fm.stringWidth(trial) > maxWidth && !line.isEmpty()) {
          lines.add(line.toString());
          line = new StringBuilder(word);
        } else {
          line = new StringBuilder(trial);
        }
      }
      if (!line.isEmpty()) lines.add(line.toString());
    }
    for (String line : lines) {
      g.drawString(line, cx - fm.stringWidth(line) / 2, y);
      y += lineHeight;
    }
    return y;
  }

  public static void divider(Graphics2D g, Color color, int cx, int y, int width) {
    g.setColor(color);
    g.fillRect(cx - width / 2, y, width, 3);
  }

  /** Soft radial glow disc. */
  public static void glowDisc(Graphics2D g, int cx, int cy, int r, Color core, Color edge) {
    Point2D center = new Point2D.Float(cx, cy);
    float[] dist = {0f, 0.55f, 1f};
    Color mid = new Color(
        (core.getRed() + edge.getRed()) / 2,
        (core.getGreen() + edge.getGreen()) / 2,
        (core.getBlue() + edge.getBlue()) / 2);
    RadialGradientPaint paint = new RadialGradientPaint(center, r, dist,
        new Color[]{core, mid, new Color(edge.getRed(), edge.getGreen(), edge.getBlue(), 0)});
    g.setPaint(paint);
    g.fillOval(cx - r, cy - r, r * 2, r * 2);
  }

  /** Subtle film grain so gradients don't band. */
  public static void grain(Graphics2D g, int w, int h, int alpha, long seed) {
    Random rnd = new Random(seed);
    g.setColor(new Color(255, 255, 255, alpha));
    for (int i = 0; i < w * h / 220; i++) {
      int x = rnd.nextInt(w), y = rnd.nextInt(h);
      g.fillRect(x, y, 2, 2);
    }
    g.setColor(new Color(0, 0, 0, alpha));
    for (int i = 0; i < w * h / 220; i++) {
      int x = rnd.nextInt(w), y = rnd.nextInt(h);
      g.fillRect(x, y, 2, 2);
    }
  }

  public static void footer(Graphics2D g, Font font, Color color, int cx, int y) {
    kicker(g, "WORKSHOP DISCOVERY", font, color, cx, y);
  }
}
