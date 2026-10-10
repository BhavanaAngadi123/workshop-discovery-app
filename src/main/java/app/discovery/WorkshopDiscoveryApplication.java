package app.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WorkshopDiscoveryApplication {
  public static void main(String[] args) {
    SpringApplication.run(WorkshopDiscoveryApplication.class, args);
  }
}
