package app.discovery.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
  public record RegisterRequest(
      @Email @NotBlank String email,
      @Size(min = 8, max = 100) String password,
      @NotBlank String displayName,
      String role) {}

  public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}

  public record AuthResponse(String token, Long userId, String displayName, String role) {}
}
