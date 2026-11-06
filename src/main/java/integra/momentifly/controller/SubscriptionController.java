package integra.momentifly.controller;

import integra.momentifly.dto.CreateSubscriptionRequest;
import integra.momentifly.dto.UpdateSubscriptionRequest;
import integra.momentifly.model.Subscription;
import integra.momentifly.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public Subscription createSubscription(Authentication authentication,
                                           @Valid @RequestBody CreateSubscriptionRequest createSubscriptionRequest){
        return subscriptionService.createSubscription(authentication.getName(), createSubscriptionRequest);
    }

    @PutMapping("/{id}")
    public Subscription updateSubscription(Authentication authentication, @PathVariable UUID id,
                                           @Valid @RequestBody UpdateSubscriptionRequest updateSubscriptionRequest){
        return subscriptionService.updateSubscription(id, authentication.getName(), updateSubscriptionRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(Authentication authentication, @PathVariable UUID id){
        return ResponseEntity.ok(subscriptionService.findSubscriptionById(id, authentication.getName()));
    }

    @GetMapping
    public List<Subscription> getAllSubscriptions(Authentication authentication){
        return subscriptionService.getSubscriptionsForUser(authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(Authentication authentication, @PathVariable UUID id){
        subscriptionService.deleteSubscription(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
