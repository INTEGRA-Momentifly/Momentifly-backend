package integra.momentifly.subscription.controller;

import integra.momentifly.subscription.domain.Subscription;
import integra.momentifly.subscription.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public Subscription createSubscription(@RequestBody Subscription newSubscription){
        return subscriptionService.createSubscription(newSubscription);
    }

    @PutMapping("/{id}")
    public Subscription updateSubscription(@PathVariable Long id, @RequestBody Subscription updatedSubscription){
        return subscriptionService.updateSubscription(id, updatedSubscription);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> findSubscriptionById(@PathVariable Long id){
        return subscriptionService.findSubscriptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Subscription> getAllSubscriptions(){
        return subscriptionService.getAllSubscriptions();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id){
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<Subscription> getSubscriptionsByUserId(@PathVariable Long userId){
        return subscriptionService.getSubscriptionsByUserId(userId);
    }
}
