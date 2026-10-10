package app.discovery.poster.templates;

import app.discovery.poster.FontSet;
import app.discovery.poster.PosterDraw;
import app.discovery.poster.PosterSpec;
import app.discovery.poster.PosterTemplate;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

/** Warm morning gradient with a glowing sun — cozy, inviting, energetic. */
public class SunriseTemplate implements PosterTemplate {
  @Override public String id() { return "sunrise"; }
  @Override public String name() { return "Sunrise Glow"; }
  @Override public String blurb() { return "Warm morning gradient with a glowing sun — cozy and inviting."; }

  @Override
  public void render(Graphics2D g, PosterSpec spec, int w, int h, FontSet fonts) {
    PosterDraw.prep(g);
    GradientPaint bg = new GradientPaint(0, 0, new Color(0xFF, 0xE3, 0xB3),
        0, h, new Color(0xE8, 0x7E, 0x9B));
    g.setPaint(bg);
    g.fillRect(0, 0, w, h);

    // sun with halo, upper third
    PosterDraw.glowDisc(g, w / 2, 380, 260, new Color(255, 246, 220), new Color(255, 200, 140));
    g.setColor(new Color(255, 250, 235));
    g.fillOval(w / 2 - 130, 380 - 130, 260, 260);

    // soft hills silhouette
    g.setColor(new Color(120, 60, 90, 90));
    g.fillOval(-w / 3, h - 260, (int) (w * 1.7), 520);

    Color ink = new Color(0x4A, 0x24, 0x33);
    Color softInk = new Color(0x4A, 0x24, 0x33, 200);

    PosterDraw.kicker(g, spec.kicker(), fonts.sans(34, true), softInk, w / 2, 150);
    int y = PosterDraw.wrappedCenter(g, spec.title(), fonts.serif(104, false), ink, w / 2, 700, w - 160, 118);
    PosterDraw.divider(g, new Color(255, 250, 235, 220), w / 2, y + 30, 120);

    y += 100;
    y = PosterDraw.wrappedCenter(g, spec.dateLine(), fonts.sans(40, true), ink, w / 2, y, w - 200, 56);
    y = PosterDraw.wrappedCenter(g, spec.priceLine(), fonts.serif(52, false), ink, w / 2, y + 8, w - 200, 64);
    y = PosterDraw.wrappedCenter(g, spec.hostLine(), fonts.sans(36, false), softInk, w / 2, y + 16, w - 200, 52);

    PosterDraw.footer(g, fonts.sans(30, true), new Color(255, 250, 235, 230), w / 2, h - 70);
    PosterDraw.grain(g, w, h, 10, 42L);
  }
}
