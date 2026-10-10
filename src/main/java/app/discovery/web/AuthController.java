package app.discovery.web;

import app.discovery.domain.Role;
import app.discovery.service.AuthService;
import app.discovery.web.dto.AuthDtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {
  private final AuthService auth;

  public AuthController(AuthService auth) {
    this.auth = auth;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Register as CUSTOMER, HOST, or ADMIN")
  public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
    Role role = "HOST".equalsIgnoreCase(req.role()) ? Role.HOST : Role.CUSTOMER;
    var r = auth.register(req.email(), req.password(), req.displayName(), role);
    return new AuthResponse(r.token(), r.userId(), r.displayName(), r.role());
  }

  @PostMapping("/login")
  @Operation(summary = "Login, returns JWT")
  public AuthResponse login(@Valid @RequestBody LoginRequest req) {
    var r = auth.login(req.email(), req.password());
    return new AuthResponse(r.token(), r.userId(), r.displayName(), r.role());
  }
}
