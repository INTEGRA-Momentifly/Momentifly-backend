package integra.momentifly.controller;

import integra.momentifly.dto.CreateSubscriptionRequest;
import integra.momentifly.dto.UpdateSubscriptionRequest;
import integra.momentifly.model.Subscription;
import integra.momentifly.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public Subscription createSubscription(@Valid @RequestBody CreateSubscriptionRequest createSubscriptionRequest){
        return subscriptionService.createSubscription(createSubscriptionRequest);
    }

    @PutMapping("/{id}")
    public Subscription updateSubscription(@PathVariable UUID id, @Valid @RequestBody UpdateSubscriptionRequest updateSubscriptionRequest){
        return subscriptionService.updateSubscription(id, updateSubscriptionRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable UUID id){
        return subscriptionService.findSubscriptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Subscription> getAllSubscriptions(@RequestParam(required = false) UUID userId){
        if(userId != null){
            return subscriptionService.getSubscriptionsByUserId(userId);
        }
        return subscriptionService.getAllSubscriptions();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable UUID id){
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

}
