package app.discovery.web;

import app.discovery.domain.User;
import app.discovery.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments")
public class PaymentController {
  private final PaymentService payments;

  public PaymentController(PaymentService payments) {
    this.payments = payments;
  }

  @PostMapping("/intent")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a Stripe PaymentIntent (test mode) for my booking")
  public IntentDto intent(@AuthenticationPrincipal User me, @RequestBody IntentRequest req) throws Exception {
    var r = payments.createIntent(me.getId(), req.bookingId());
    return new IntentDto(r.clientSecret(), r.paymentIntentId(), r.amountCents(), r.live());
  }

  public record IntentRequest(Long bookingId) {}
  public record IntentDto(String clientSecret, String paymentIntentId, long amountCents, boolean liveMode) {}

  @PostMapping("/webhook")
  @Operation(summary = "Stripe webhook (signature-verified when live)")
  public void webhook(@RequestBody String payload,
                      @RequestHeader(value = "Stripe-Signature", required = false) String sig) {
    payments.handleWebhook(payload, sig);
  }

  @PostMapping("/simulate/{paymentIntentId}")
  @Operation(summary = "DEV ONLY: simulate a successful test payment (disabled when Stripe is live)")
  public void simulateById(@PathVariable String paymentIntentId) {
    payments.simulateSuccess(paymentIntentId);
  }
}
