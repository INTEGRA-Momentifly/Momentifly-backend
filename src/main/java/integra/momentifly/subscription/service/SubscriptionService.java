package integra.momentifly.subscription.service;

import integra.momentifly.subscription.domain.Subscription;
import integra.momentifly.subscription.repository.SubscriptionRepository;

import java.util.List;
import java.util.Optional;

public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription createSubscription(Subscription subscription){
        return subscriptionRepository.save(subscription);
    }

    public Subscription updateSubscription(Long id, Subscription updatedSubscription){
        Subscription existing = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
        existing.setName(updatedSubscription.getName());
        existing.setCost(updatedSubscription.getCost());
        existing.setEndDate(updatedSubscription.getEndDate());
        existing.setUser(updatedSubscription.getUser());

        return subscriptionRepository.save(existing);
    }

    public void deleteSubscription(Long id){
        subscriptionRepository.deleteById(id);
    }

    public Optional<Subscription> findSubscriptionById(Long id){
        return subscriptionRepository.findById(id);
    }

    public List<Subscription> getAllSubscriptions(){
        return subscriptionRepository.findAll();
    }

    //this is based on the diagram suggested method
    public List<Subscription> getSubscriptionsByUserId(Long user_id){
        return subscriptionRepository.findByUser_Id(user_id);
    }
}
