package app.discovery.ui;

import app.discovery.domain.User;
import app.discovery.repo.UserRepository;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;

/** Minimal session auth for the Vaadin UI (the REST API uses JWT independently). */
@Component
public class SessionUtil {
  private final UserRepository users;

  public SessionUtil(UserRepository users) {
    this.users = users;
  }

  public void login(Long userId) {
    VaadinSession.getCurrent().setAttribute("userId", userId);
  }

  public void logout() {
    VaadinSession.getCurrent().setAttribute("userId", null);
  }

  public User currentUser() {
    Long id = (Long) VaadinSession.getCurrent().getAttribute("userId");
    return id == null ? null : users.findById(id).orElse(null);
  }

  public boolean isHostOrAdmin() {
    User u = currentUser();
    return u != null && (u.getRole().name().equals("HOST") || u.getRole().name().equals("ADMIN"));
  }
}
