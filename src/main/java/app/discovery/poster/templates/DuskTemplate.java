package app.discovery.poster.templates;

import app.discovery.poster.FontSet;
import app.discovery.poster.PosterDraw;
import app.discovery.poster.PosterSpec;
import app.discovery.poster.PosterTemplate;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.Random;

/** Indigo night with moon and stars — dreamy, premium, evening workshops. */
public class DuskTemplate implements PosterTemplate {
  @Override public String id() { return "dusk"; }
  @Override public String name() { return "Dusk Dream"; }
  @Override public String blurb() { return "Indigo night sky with moon and stars — dreamy and premium."; }

  @Override
  public void render(Graphics2D g, PosterSpec spec, int w, int h, FontSet fonts) {
    PosterDraw.prep(g);
    GradientPaint bg = new GradientPaint(0, 0, new Color(0x1B, 0x23, 0x40), 0, h, new Color(0x5B, 0x4A, 0x7A));
    g.setPaint(bg);
    g.fillRect(0, 0, w, h);

    Random rnd = new Random(spec.paletteVariant() * 104729L + 3);
    // stars
    for (int i = 0; i < 90; i++) {
      int x = rnd.nextInt(w), y = rnd.nextInt(h - 300);
      int s = 2 + rnd.nextInt(4);
      g.setColor(new Color(255, 255, 255, 60 + rnd.nextInt(140)));
      g.fillOval(x, y, s, s);
    }
    // moon with glow
    PosterDraw.glowDisc(g, w - 260, 300, 200, new Color(240, 238, 255), new Color(150, 140, 220));
    g.setColor(new Color(0xF0, 0xEE, 0xFF));
    g.fillOval(w - 260 - 80, 300 - 80, 160, 160);

    Color lavender = new Color(0xE8, 0xE2, 0xFF);
    Color softLav = new Color(0xE8, 0xE2, 0xFF, 200);
    Color gold = new Color(0xE8, 0xC9, 0x7A);

    PosterDraw.kicker(g, spec.kicker(), fonts.sans(34, true), gold, w / 2, 170);
    int y = PosterDraw.wrappedCenter(g, spec.title(), fonts.serif(100, false), lavender, w / 2, 640, w - 180, 114);
    PosterDraw.divider(g, gold, w / 2, y + 30, 120);

    y += 100;
    y = PosterDraw.wrappedCenter(g, spec.dateLine(), fonts.sans(40, true), lavender, w / 2, y, w - 200, 56);
    y = PosterDraw.wrappedCenter(g, spec.priceLine(), fonts.serif(52, false), gold, w / 2, y + 8, w - 200, 64);
    y = PosterDraw.wrappedCenter(g, spec.hostLine(), fonts.sans(36, false), softLav, w / 2, y + 16, w - 200, 52);

    PosterDraw.footer(g, fonts.sans(30, true), new Color(0xE8, 0xE2, 0xFF, 170), w / 2, h - 70);
    PosterDraw.grain(g, w, h, 12, 99L);
  }
}
