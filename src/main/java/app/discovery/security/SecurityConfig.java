package app.discovery.security;

import app.discovery.ui.LoginView;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Two filter chains:
 * 1. REST API (/api/**): stateless JWT, permitAll for public endpoints
 * 2. Vaadin UI: session login via LoginView, role annotations on views
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {
  private final JwtAuthFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  @Order(1)
  SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/api/**");
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers("/api/payments/webhook").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/workshops/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/feed/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/seasons/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .requestMatchers("/api/host/**").hasAnyRole("HOST", "ADMIN")
        .anyRequest().authenticated());
    http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.csrf(csrf -> csrf.disable());
    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  @Order(2)
  SecurityFilterChain vaadinChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/posters/**").permitAll()
        .requestMatchers("/swagger-ui/**").permitAll()
        .requestMatchers("/v3/api-docs/**").permitAll());
    http.with(VaadinSecurityConfigurer.vaadin(), c -> c.loginView(LoginView.class));
    return http.build();
  }
}
