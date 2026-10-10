package app.discovery.ui;

import app.discovery.ai.AiConciergeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.UUID;

/** Sunny, the AI concierge — vague intent welcome. */
@Route(value = "ask", layout = MainLayout.class)
@PageTitle("AI Concierge")
@AnonymousAllowed
public class ConciergeView extends VerticalLayout {
  private final AiConciergeService concierge;
  private final VerticalLayout messages = new VerticalLayout();
  private final String sessionId = UUID.randomUUID().toString();

  public ConciergeView(AiConciergeService concierge) {
    this.concierge = concierge;
    setPadding(true);
    setHeightFull();

    H3 title = new H3("Ask Sunny ☀️");
    Paragraph sub = new Paragraph("Tell me your idea — even a vague one — and I'll find your perfect workshop.");
    sub.getStyle().set("color", "var(--lumo-secondary-text-color)");

    messages.setHeight("55vh");
    messages.getStyle().set("overflow-y", "auto").set("border", "1px solid var(--lumo-contrast-10pct)")
        .set("border-radius", "16px").set("padding", "12px");
    bubble("Sunny", "Hi! What are you in the mood for? A pottery date, pumpkin picking, pasta night... just say it in your words.", false);

    TextField input = new TextField();
    input.setPlaceholder("I don't know what to do this weekend...");
    input.setWidthFull();
    Button send = new Button("Send", e -> send(input));
    input.addKeyDownListener(com.vaadin.flow.component.Key.ENTER, e -> send(input));
    HorizontalLayout bar = new HorizontalLayout(input, send);
    bar.setWidthFull();
    bar.setFlexGrow(1, input);

    add(title, sub, messages, bar);
  }

  private void send(TextField input) {
    String text = input.getValue();
    if (text == null || text.isBlank()) return;
    input.clear();
    bubble("You", text, true);
    Div thinking = bubble("Sunny", "thinking...", false);
    // run off the UI thread; AI can take a few seconds
    new Thread(() -> {
      try {
        var r = concierge.chat(sessionId, text);
        getUI().ifPresent(ui -> ui.access(() -> {
          messages.remove(thinking);
          String suffix = r.aiPowered() ? "" : "\n\n_(quick picks — start the AI service for full conversation)_";
          bubble("Sunny", r.reply() + suffix, false);
        }));
      } catch (Exception ex) {
        getUI().ifPresent(ui -> ui.access(() -> {
          messages.remove(thinking);
          bubble("Sunny", "Hmm, I stumbled — try again?", false);
        }));
      }
    }).start();
  }

  private Div bubble(String who, String text, boolean mine) {
    Div b = new Div();
    b.getStyle()
        .set("max-width", "75%").set("padding", "10px 14px").set("border-radius", "16px")
        .set("margin-bottom", "8px").set("white-space", "pre-wrap")
        .set("align-self", mine ? "flex-end" : "flex-start")
        .set("background", mine ? "var(--lumo-primary-color)" : "var(--lumo-contrast-5pct)")
        .set("color", mine ? "white" : "inherit");
    Paragraph p = new Paragraph(text);
    p.getStyle().set("margin", "0");
    b.add(p);
    messages.add(b);
    return b;
  }
}
