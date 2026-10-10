package app.discovery.poster;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

/** Loads bundled display fonts (OFL-licensed) once. */
public final class FontSet {
  public final Font serif;
  public final Font sans;

  private FontSet(Font serif, Font sans) {
    this.serif = serif;
    this.sans = sans;
  }

  private static Font load(String path) {
    try (InputStream in = FontSet.class.getResourceAsStream(path)) {
      if (in == null) return new Font(Font.SERIF, Font.PLAIN, 12);
      return Font.createFont(Font.TRUETYPE_FONT, in);
    } catch (FontFormatException | IOException e) {
      return new Font(Font.SERIF, Font.PLAIN, 12);
    }
  }

  public static FontSet load() {
    return new FontSet(load("/fonts/PlayfairDisplay.ttf"), load("/fonts/Inter.ttf"));
  }

  public Font serif(float size, boolean bold) {
    return serif.deriveFont(bold ? Font.BOLD : Font.PLAIN, size);
  }

  public Font sans(float size, boolean bold) {
    return sans.deriveFont(bold ? Font.BOLD : Font.PLAIN, size);
  }
}
