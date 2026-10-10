package app.discovery.web;

import app.discovery.ai.AiConciergeService;
import app.discovery.web.dto.ConciergeDtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/concierge")
@Tag(name = "AI Concierge")
public class ConciergeController {
  private final AiConciergeService concierge;

  public ConciergeController(AiConciergeService concierge) {
    this.concierge = concierge;
  }

  @PostMapping("/chat")
  @Operation(summary = "Chat with Sunny, the AI concierge (vague intent OK: 'plan a pottery date')")
  public ChatResponse chat(@Valid @RequestBody ChatRequest req) {
    var r = concierge.chat(req.sessionId(), req.message());
    return new ChatResponse(r.reply(), r.aiPowered());
  }
}
