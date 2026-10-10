package app.discovery.repo;

import app.discovery.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
}
