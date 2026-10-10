package app.discovery.ui;

import app.discovery.domain.Role;
import app.discovery.service.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("register")
@PageTitle("Create account")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {
  public RegisterView(AuthService auth, SessionUtil session) {
    setAlignItems(Alignment.CENTER);
    EmailField email = new EmailField("Email");
    email.setRequiredIndicatorVisible(true);
    PasswordField password = new PasswordField("Password (min 8 chars)");
    TextField name = new TextField("Display name");
    ComboBox<String> role = new ComboBox<>("I want to", "Book workshops", "Host workshops");
    role.setValue("Book workshops");
    Button go = new Button("Create account", e -> {
      try {
        Role r = "Host workshops".equals(role.getValue()) ? Role.HOST : Role.CUSTOMER;
        var res = auth.register(email.getValue(), password.getValue(), name.getValue(), r);
        session.login(res.userId());
        UI.getCurrent().navigate("");
      } catch (Exception ex) {
        Notification.show(ex.getMessage(), 4000, Notification.Position.MIDDLE);
      }
    });
    FormLayout form = new FormLayout(email, password, name, role);
    form.setMaxWidth("420px");
    add(new H2("Join Workshop Discovery"), form, go);
  }
}
