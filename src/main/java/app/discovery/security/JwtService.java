package app.discovery.security;

import app.discovery.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/** JWT issuance + verification. Secret comes from env, never code. */
@Service
public class JwtService {
  private final SecretKey key;
  private final long ttlMs;

  public JwtService(@Value("${app.jwt.secret:change-me-in-production-use-env-APP_JWT_SECRET}") String secret,
                    @Value("${app.jwt.ttl-ms:86400000}") long ttlMs) {
    if (secret == null || secret.length() < 32) {
      throw new IllegalStateException("app.jwt.secret must be >= 32 chars (set APP_JWT_SECRET env)");
    }
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.ttlMs = ttlMs;
  }

  public String issue(User user) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
        .subject(String.valueOf(user.getId()))
        .claim("email", user.getEmail())
        .claim("role", user.getRole().name())
        .issuedAt(new Date(now))
        .expiration(new Date(now + ttlMs))
        .signWith(key)
        .compact();
  }

  public Claims parse(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }
}
