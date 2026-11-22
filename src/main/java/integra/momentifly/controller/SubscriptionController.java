package integra.momentifly.controller;

import integra.momentifly.dto.CreateSubscriptionRequest;
import integra.momentifly.dto.UpdateSubscriptionRequest;
import integra.momentifly.model.Subscription;
import integra.momentifly.service.SubscriptionService;
import integra.momentifly.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

  private final SubscriptionService subscriptionService;
  private final UserService userService;

  @PostMapping
  public Subscription createSubscription(Authentication authentication,
                                           @Valid @RequestBody CreateSubscriptionRequest createSubscriptionRequest) {
    return subscriptionService.createSubscription(authentication.getName(), createSubscriptionRequest);
  }

  @PutMapping("/{id}")
  public Subscription updateSubscription(Authentication authentication, @PathVariable UUID id,
                                           @Valid @RequestBody UpdateSubscriptionRequest updateSubscriptionRequest) {
    return subscriptionService.updateSubscription(id, authentication.getName(), updateSubscriptionRequest);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Subscription> getSubscriptionById(Authentication authentication,@PathVariable UUID id) {
    return ResponseEntity.ok(subscriptionService.findSubscriptionById(id, authentication.getName()));
  }

  @GetMapping
  public List<Subscription> getAllSubscriptions(Authentication authentication) {
    UUID userId = userService.getUserByUsername(authentication.getName()).getId();

    return subscriptionService.getSubscriptionsByUserId(userId);
  }

  @GetMapping("/active")
  public List<Subscription> getAllActiveSubscriptions(Authentication authentication) {
    UUID userId = userService.getUserByUsername(authentication.getName()).getId();

    return subscriptionService.getActiveSubscriptionsByUserId(userId);
  }

  @GetMapping("/expired")
  public List<Subscription> getAllExpiredSubscriptions(Authentication authentication) {
    UUID userId = userService.getUserByUsername(authentication.getName()).getId();

    return subscriptionService.getExpiredSubscriptionsByUserId(userId);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSubscription(Authentication authentication,@PathVariable UUID id) {
    subscriptionService.deleteSubscription(id, authentication.getName());

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/autoRenewal")
  public Subscription updateAutoRenewal(
      @PathVariable UUID id,
      @RequestParam boolean autoRenewal) {
    return subscriptionService.updateAutoRenewal(id, autoRenewal);
  }
}
