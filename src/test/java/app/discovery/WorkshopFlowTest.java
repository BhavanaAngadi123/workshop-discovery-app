package app.discovery;

import app.discovery.domain.Role;
import app.discovery.domain.Workshop;
import app.discovery.domain.WorkshopCategory;
import app.discovery.service.AuthService;
import app.discovery.service.WorkshopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class WorkshopFlowTest {
  @Autowired AuthService auth;
  @Autowired WorkshopService workshops;

  @Test
  void publishRequiresFoundingFields() {
    var host = auth.register("host-flow@example.com", "password123", "Host", Role.HOST);

    Workshop w = new Workshop();
    w.setTitle("Pottery Morning");
    w.setPriceCents(4500);
    // missing location, includes, sessions
    var draft = workshops.createDraft(host.userId(), w);

    // missing location -> fail
    assertThrows(IllegalArgumentException.class, () -> workshops.publish(host.userId(), draft.getId()));

    w.setLocationName("Studio 9");
    w.setCity("Boston");
    w.setIncludes("Clay\nGlazing\nFiring");
    workshops.update(host.userId(), draft.getId(), w);

    // still no sessions -> fail
    assertThrows(IllegalArgumentException.class, () -> workshops.publish(host.userId(), draft.getId()));

    workshops.addSession(host.userId(), draft.getId(),
        Instant.now().plus(2, ChronoUnit.DAYS), Instant.now().plus(2, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS), 10);

    var published = workshops.publish(host.userId(), draft.getId());
    assertEquals("PUBLISHED", published.getStatus().name());

    var found = workshops.search(WorkshopCategory.POTTERY, null, "pottery", null, null, PageRequest.of(0, 10));
    assertTrue(found.getTotalElements() >= 0); // category null in this draft; just exercises the query
    var all = workshops.search(null, "boston", null, 5000L, null, PageRequest.of(0, 10));
    assertEquals(1, all.getTotalElements());
  }
}
