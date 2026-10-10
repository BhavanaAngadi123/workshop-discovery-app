package app.discovery.poster;

/** Everything the poster needs. Rendered server-side to PNG, no external APIs. */
public record PosterSpec(
    String title,
    String kicker,
    String dateLine,
    String priceLine,
    String hostLine,
    String templateId,
    int paletteVariant
) {
  public PosterSpec {
    if (title == null || title.isBlank()) title = "Untitled Workshop";
    if (kicker == null) kicker = "";
    if (dateLine == null) dateLine = "";
    if (priceLine == null) priceLine = "";
    if (hostLine == null) hostLine = "";
    if (templateId == null || templateId.isBlank()) templateId = "sunrise";
  }

  public String footer() { return "WORKSHOP DISCOVERY"; }
}
