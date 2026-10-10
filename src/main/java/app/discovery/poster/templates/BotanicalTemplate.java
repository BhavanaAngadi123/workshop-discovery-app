package app.discovery.poster.templates;

import app.discovery.poster.FontSet;
import app.discovery.poster.PosterDraw;
import app.discovery.poster.PosterSpec;
import app.discovery.poster.PosterTemplate;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

/** Deep botanical green with hand-drawn leaf sprigs — calm, organic, artisanal. */
public class BotanicalTemplate implements PosterTemplate {
  @Override public String id() { return "botanical"; }
  @Override public String name() { return "Botanical Calm"; }
  @Override public String blurb() { return "Deep forest green with leaf sprigs — calm and artisanal."; }

  private void leaf(Graphics2D g, int x, int y, int len, int wid, double angle, Color c) {
    AffineTransform old = g.getTransform();
    g.translate(x, y);
    g.rotate(angle);
    g.setColor(c);
    g.fillOval(0, -wid / 2, len, wid);
    g.setTransform(old);
  }

  private void sprig(Graphics2D g, int x, int y, double angle, int size, Color c, Random rnd) {
    for (int i = 0; i < 7; i++) {
      int lx = x + (int) (Math.cos(angle) * i * size / 7);
      int ly = y + (int) (Math.sin(angle) * i * size / 7);
      leaf(g, lx, ly, size / 3 + rnd.nextInt(size / 6), size / 7, angle + (rnd.nextBoolean() ? 0.7 : -0.7), c);
    }
  }

  @Override
  public void render(Graphics2D g, PosterSpec spec, int w, int h, FontSet fonts) {
    PosterDraw.prep(g);
    GradientPaint bg = new GradientPaint(0, 0, new Color(0x0E, 0x3B, 0x2E), 0, h, new Color(0x1F, 0x6B, 0x4F));
    g.setPaint(bg);
    g.fillRect(0, 0, w, h);

    Random rnd = new Random(spec.paletteVariant() * 7919L + 7);
    Color leaf1 = new Color(0x2E, 0x8B, 0x62, 160);
    Color leaf2 = new Color(0xD9, 0xB9, 0x7A, 130);
    sprig(g, 120, 240, 0.5, 420, leaf1, rnd);
    sprig(g, w - 100, 420, Math.PI - 0.4, 460, leaf1, rnd);
    sprig(g, 200, h - 200, -0.9, 380, leaf2, rnd);
    sprig(g, w - 180, h - 320, Math.PI + 0.8, 400, leaf2, rnd);

    Color cream = new Color(0xF5, 0xEF, 0xDD);
    Color gold = new Color(0xD9, 0xB9, 0x7A);
    Color softCream = new Color(0xF5, 0xEF, 0xDD, 200);

    PosterDraw.kicker(g, spec.kicker(), fonts.sans(34, true), gold, w / 2, 170);
    int y = PosterDraw.wrappedCenter(g, spec.title(), fonts.serif(100, false), cream, w / 2, 620, w - 180, 114);
    PosterDraw.divider(g, gold, w / 2, y + 30, 120);

    y += 100;
    y = PosterDraw.wrappedCenter(g, spec.dateLine(), fonts.sans(40, true), cream, w / 2, y, w - 200, 56);
    y = PosterDraw.wrappedCenter(g, spec.priceLine(), fonts.serif(52, false), gold, w / 2, y + 8, w - 200, 64);
    y = PosterDraw.wrappedCenter(g, spec.hostLine(), fonts.sans(36, false), softCream, w / 2, y + 16, w - 200, 52);

    PosterDraw.footer(g, fonts.sans(30, true), new Color(0xF5, 0xEF, 0xDD, 180), w / 2, h - 70);
    PosterDraw.grain(g, w, h, 12, 77L);
  }
}
