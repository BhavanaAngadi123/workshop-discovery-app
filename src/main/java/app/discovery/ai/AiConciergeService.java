package app.discovery.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI concierge bot: Spring AI + local Ollama (zero paid APIs). Handles vague intent
 * ("I don't know what to do this weekend", "plan a pottery date"), recommends
 * workshops via tools, curates from real reviews/ratings. Degrades gracefully:
 * if Ollama is unreachable, falls back to a helpful template response with
 * direct recommendations (the app stays usable).
 */
@Service
public class AiConciergeService {
  private static final Logger log = LoggerFactory.getLogger(AiConciergeService.class);

  private static final String SYSTEM = """
      You are Sunny, the friendly concierge of Workshop Discovery, an app for finding
      and booking local hands-on workshops (pottery, cooking, gardening, art, seasonal
      activities like pumpkin picking). Be warm, concise, and specific.
      - Always use the search tools to find REAL workshops; never invent workshops, prices, or links.
      - When the user gives a budget, pass it as maxPriceCents and prefer options that fit.
      - For vague requests ("something fun this weekend", "date idea"), ask ONE clarifying
        question OR search broadly and present 3 diverse picks.
      - Mention ratings and the 'why' reason for each pick.
      - Keep replies under 120 words unless the user asks for detail.
      - If no workshops match, say so honestly and suggest nearby categories.
      """;

  private final ObjectProvider<ChatClient.Builder> chatClientBuilder;
  private final ConciergeTools tools;
  private final Map<String, List<Map<String, String>>> histories = new ConcurrentHashMap<>();

  public AiConciergeService(ObjectProvider<ChatClient.Builder> chatClientBuilder, ConciergeTools tools) {
    this.chatClientBuilder = chatClientBuilder;
    this.tools = tools;
  }

  public record ChatReply(String reply, boolean aiPowered) {}

  /** Chat with per-session memory (sessionId from the client). */
  public ChatReply chat(String sessionId, String message) {
    var builder = chatClientBuilder.getIfAvailable();
    if (builder != null) {
      try {
        var history = histories.computeIfAbsent(sessionId, k -> new ArrayList<Map<String, String>>());
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(SYSTEM));
        for (var m : history) {
          messages.add("assistant".equals(m.get("role"))
              ? new AssistantMessage(m.get("content"))
              : new UserMessage(m.get("content")));
        }
        messages.add(new UserMessage(message));
        String reply = builder.build().prompt().messages(messages).tools(tools).call().content();
        history.add(Map.of("role", "user", "content", message));
        history.add(Map.of("role", "assistant", "content", reply == null ? "" : reply));
        if (history.size() > 20) history.subList(0, history.size() - 20).clear();
        return new ChatReply(reply, true);
      } catch (Exception e) {
        log.info("Ollama unreachable ({}); using concierge fallback", e.getMessage());
      }
    }
    return new ChatReply(fallback(message), false);
  }

  /** Honest fallback when no LLM is available: direct tool-driven picks. */
  private String fallback(String message) {
    try {
      var picks = tools.searchWorkshops(message, null, null);
      if (picks.isEmpty()) {
        return "I couldn't find a workshop matching that right now — try 'pottery', 'pasta making', "
            + "'pumpkin picking', or a city name. (Tip: start the AI service for smarter help.)";
      }
      StringBuilder sb = new StringBuilder("Here are some lovely options I found:\n");
      picks.stream().limit(3).forEach(p ->
          sb.append("• ").append(p.get("title")).append(" — ").append(p.get("price"))
              .append(" (").append(p.get("why")).append(")\n"));
      sb.append("(Tip: start the AI service for conversational help.)");
      return sb.toString();
    } catch (Exception e) {
      return "Hmm, I'm having trouble right now — please try the search page. (Tip: start the AI service for conversational help.)";
    }
  }
}
