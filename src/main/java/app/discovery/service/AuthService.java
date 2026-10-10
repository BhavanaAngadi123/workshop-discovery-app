package app.discovery.service;

import app.discovery.domain.HostProfile;
import app.discovery.domain.Role;
import app.discovery.domain.User;
import app.discovery.repo.HostProfileRepository;
import app.discovery.repo.UserRepository;
import app.discovery.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
  private final UserRepository users;
  private final HostProfileRepository hosts;
  private final PasswordEncoder passwords;
  private final JwtService jwt;
  private final AuditService audit;

  public AuthService(UserRepository users, HostProfileRepository hosts,
                     PasswordEncoder passwords, JwtService jwt, AuditService audit) {
    this.users = users;
    this.hosts = hosts;
    this.passwords = passwords;
    this.jwt = jwt;
    this.audit = audit;
  }

  @Transactional
  public AuthResult register(String email, String password, String displayName, Role role) {
    String cleanEmail = email == null ? "" : email.trim().toLowerCase();
    if (!cleanEmail.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
      throw new IllegalArgumentException("Invalid email address");
    }
    if (password == null || password.length() < 8) {
      throw new IllegalArgumentException("Password must be at least 8 characters");
    }
    if (users.existsByEmail(cleanEmail)) {
      throw new IllegalArgumentException("Email already registered");
    }
    User user = new User();
    user.setEmail(cleanEmail);
    user.setPasswordHash(passwords.encode(password));
    user.setDisplayName(displayName == null || displayName.isBlank() ? cleanEmail : displayName.trim());
    user.setRole(role == null ? Role.CUSTOMER : role);
    users.save(user);
    if (user.getRole() == Role.HOST) {
      HostProfile hp = new HostProfile();
      hp.setUser(user);
      hosts.save(hp);
    }
    audit.log(user.getId(), "USER_REGISTER", "User", user.getId());
    return new AuthResult(jwt.issue(user), user.getId(), user.getDisplayName(), user.getRole().name());
  }

  @Transactional(readOnly = true)
  public AuthResult login(String email, String password) {
    String cleanEmail = email == null ? "" : email.trim().toLowerCase();
    User user = users.findByEmail(cleanEmail)
        .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    if (user.isLocked() || !passwords.matches(password, user.getPasswordHash())) {
      audit.log(user.getId(), "LOGIN_FAILED", "User", user.getId());
      throw new IllegalArgumentException("Invalid email or password");
    }
    audit.log(user.getId(), "LOGIN", "User", user.getId());
    return new AuthResult(jwt.issue(user), user.getId(), user.getDisplayName(), user.getRole().name());
  }

  public record AuthResult(String token, Long userId, String displayName, String role) {}
}
