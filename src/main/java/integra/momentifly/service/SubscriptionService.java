package integra.momentifly.service;

import integra.momentifly.dto.CreateSubscriptionRequest;
import integra.momentifly.dto.UpdateSubscriptionRequest;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.Subscription;
import integra.momentifly.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    public Subscription createSubscription(String username, CreateSubscriptionRequest createSubscriptionRequest){
        Subscription newSubscription = new Subscription();
        newSubscription.setUser(userService.getUserByUsername(username));
        newSubscription.setCost(createSubscriptionRequest.getCost());
        newSubscription.setName(createSubscriptionRequest.getName());
        newSubscription.setEndDate(createSubscriptionRequest.getEndDate());
        return subscriptionRepository.save(newSubscription);
    }

    public Subscription updateSubscription(UUID id, String username, UpdateSubscriptionRequest updateSubscriptionRequest){
        Subscription existingSubscription = findSubscriptionById(id, username);
        existingSubscription.setName(updateSubscriptionRequest.getName());
        existingSubscription.setCost(updateSubscriptionRequest.getCost());
        existingSubscription.setEndDate(updateSubscriptionRequest.getEndDate());
        return subscriptionRepository.save(existingSubscription);
    }

    public void deleteSubscription(UUID id, String username){
        subscriptionRepository.delete(findSubscriptionById(id, username));
    }

    public Subscription findSubscriptionById(UUID id, String username){
        return subscriptionRepository.findByIdAndUser_Username(id, username)
                .orElseThrow(() -> new NotFoundException("Subscription not found"));
    }

    public List<Subscription> getSubscriptionsForUser(String username){
        return subscriptionRepository.findByUser_Username(username);
    }
}
