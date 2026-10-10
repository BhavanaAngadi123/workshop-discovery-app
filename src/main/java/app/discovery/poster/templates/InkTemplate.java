package app.discovery.poster.templates;

import app.discovery.poster.FontSet;
import app.discovery.poster.PosterDraw;
import app.discovery.poster.PosterSpec;
import app.discovery.poster.PosterTemplate;

import java.awt.Color;
import java.awt.Graphics2D;

/** Warm paper, bold ink typography, one terracotta accent — editorial, confident. */
public class InkTemplate implements PosterTemplate {
  @Override public String id() { return "ink"; }
  @Override public String name() { return "Editorial Ink"; }
  @Override public String blurb() { return "Warm paper with bold ink type — editorial and confident."; }

  @Override
  public void render(Graphics2D g, PosterSpec spec, int w, int h, FontSet fonts) {
    PosterDraw.prep(g);
    g.setColor(new Color(0xF7, 0xF2, 0xE9));
    g.fillRect(0, 0, w, h);

    Color ink = new Color(0x1E, 0x1B, 0x16);
    Color accent = new Color(0xC4, 0x5A, 0x3C);
    Color softInk = new Color(0x1E, 0x1B, 0x16, 170);

    // accent bar top
    g.setColor(accent);
    g.fillRect(90, 90, 14, 220);
    // thin frame
    g.setColor(new Color(0x1E, 0x1B, 0x16, 40));
    g.drawRect(48, 48, w - 96, h - 96);

    PosterDraw.kicker(g, spec.kicker(), fonts.sans(34, true), accent, w / 2, 200);
    int y = PosterDraw.wrappedCenter(g, spec.title(), fonts.serif(112, true), ink, w / 2, 560, w - 200, 124);
    PosterDraw.divider(g, accent, w / 2, y + 34, 140);

    y += 110;
    y = PosterDraw.wrappedCenter(g, spec.dateLine(), fonts.sans(42, true), ink, w / 2, y, w - 220, 58);
    // price in accent
    y = PosterDraw.wrappedCenter(g, spec.priceLine(), fonts.serif(56, true), accent, w / 2, y + 10, w - 220, 68);
    y = PosterDraw.wrappedCenter(g, spec.hostLine(), fonts.sans(36, false), softInk, w / 2, y + 18, w - 220, 52);

    PosterDraw.footer(g, fonts.sans(30, true), softInk, w / 2, h - 90);
    PosterDraw.grain(g, w, h, 8, 13L);
  }
}
