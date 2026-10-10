package app.discovery.ui;

import app.discovery.service.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Log in")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
  private final AuthService auth;
  private final SessionUtil session;
  private final LoginForm form = new LoginForm();

  public LoginView(AuthService auth, SessionUtil session) {
    this.auth = auth;
    this.session = session;
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
    H2 h = new H2("Welcome back");
    Paragraph p = new Paragraph("Log in to book workshops, post experiences, and host your own.");
    form.addLoginListener(e -> {
      try {
        var r = auth.login(e.getUsername(), e.getPassword());
        session.login(r.userId());
        UI.getCurrent().navigate("");
      } catch (Exception ex) {
        form.setError(true);
      }
    });
    Button register = new Button("Create account", e -> UI.getCurrent().navigate("register"));
    add(h, p, form, register);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (session.currentUser() != null) event.forwardTo("");
  }
}
