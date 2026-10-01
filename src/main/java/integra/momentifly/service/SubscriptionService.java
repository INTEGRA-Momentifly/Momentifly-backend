package integra.momentifly.service;

import integra.momentifly.dto.CreateSubscriptionRequest;
import integra.momentifly.dto.UpdateSubscriptionRequest;
import integra.momentifly.model.Subscription;
import integra.momentifly.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription createSubscription(CreateSubscriptionRequest createSubscriptionRequest){

//        TEMPORAR: Nu setăm user-ul până nu este gata modulul de User/UserRepository
//        Când va fi creat UserRepository, aici vei adăuga:
//        User user = userRepository.findById(createSubscriptionRequest.getUserId()).orElseThrow(...)
//        subscription.setUser(user);

        Subscription newSubscription = new Subscription();
        newSubscription.setCost(createSubscriptionRequest.getCost());
        newSubscription.setName(createSubscriptionRequest.getName());
        newSubscription.setEndDate(createSubscriptionRequest.getEndDate());

        return subscriptionRepository.save(newSubscription);
    }

    public Subscription updateSubscription(UUID id, UpdateSubscriptionRequest updateSubscriptionRequest){

        Subscription existingSubscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));

        existingSubscription.setName(updateSubscriptionRequest.getName());
        existingSubscription.setCost(updateSubscriptionRequest.getCost());
        existingSubscription.setEndDate(updateSubscriptionRequest.getEndDate());

//        Când UserRepository va fi pregătit, decomentezi liniile de mai jos:
//        User newUser = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
//        existingSubscription.setUser(newUser);

        return subscriptionRepository.save(existingSubscription);
    }

    public void deleteSubscription(UUID id){
        subscriptionRepository.deleteById(id);
    }

    public Optional<Subscription> findSubscriptionById(UUID id){
        return subscriptionRepository.findById(id);
    }

    public List<Subscription> getAllSubscriptions(){
        return subscriptionRepository.findAll();
    }

    //this is based on the diagram suggested method
    public List<Subscription> getSubscriptionsByUserId(UUID user_id){
        return subscriptionRepository.findByUserId(user_id);
    }
}
