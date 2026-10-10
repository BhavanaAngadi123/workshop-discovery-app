package app.discovery;

import app.discovery.domain.Role;
import app.discovery.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthFlowTest {
  @Autowired AuthService auth;

  @Test
  void registerAndLogin() {
    var r = auth.register("test-auth@example.com", "password123", "Test User", Role.CUSTOMER);
    assertNotNull(r.token());
    assertEquals("CUSTOMER", r.role());

    var l = auth.login("test-auth@example.com", "password123");
    assertEquals(r.userId(), l.userId());

    assertThrows(IllegalArgumentException.class,
        () -> auth.register("test-auth@example.com", "password123", "Dup", Role.CUSTOMER));
    assertThrows(IllegalArgumentException.class,
        () -> auth.login("test-auth@example.com", "wrongpass"));
  }
}
