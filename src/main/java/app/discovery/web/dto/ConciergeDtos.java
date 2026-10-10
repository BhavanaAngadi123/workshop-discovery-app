package app.discovery.web.dto;

import jakarta.validation.constraints.NotBlank;

public class ConciergeDtos {
  public record ChatRequest(@NotBlank String sessionId, @NotBlank String message) {}
  public record ChatResponse(String reply, boolean aiPowered) {}
}
