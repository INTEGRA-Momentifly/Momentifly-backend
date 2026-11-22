package integra.momentifly.service;

import integra.momentifly.dto.CreateSubscriptionRequest;
import integra.momentifly.dto.UpdateSubscriptionRequest;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.Subscription;
import integra.momentifly.model.User;
import integra.momentifly.repository.SubscriptionRepository;
import integra.momentifly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;

  public Subscription createSubscription(CreateSubscriptionRequest createSubscriptionRequest) {

    User user = userRepository.findById(createSubscriptionRequest.getUserId())
        .orElseThrow(() -> new NotFoundException(
            "User not found with id: " + createSubscriptionRequest.getUserId()));

    Subscription newSubscription = new Subscription();
    newSubscription.setUser(user);
    newSubscription.setCost(createSubscriptionRequest.getCost());
    newSubscription.setName(createSubscriptionRequest.getName());
    newSubscription.setAutoRenewal(createSubscriptionRequest.isAutoRenewal());
    newSubscription.setEndDate(createSubscriptionRequest.getEndDate());

    return subscriptionRepository.save(newSubscription);
  }

  public Subscription updateSubscription(UUID id, UpdateSubscriptionRequest updateSubscriptionRequest) {

    Subscription existingSubscription = subscriptionRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Subscription not found with id: " + id));

    existingSubscription.setName(updateSubscriptionRequest.getName());
    existingSubscription.setCost(updateSubscriptionRequest.getCost());
    existingSubscription.setAutoRenewal(updateSubscriptionRequest.isAutoRenewal());
    existingSubscription.setEndDate(updateSubscriptionRequest.getEndDate());

    return subscriptionRepository.save(existingSubscription);
  }

  public void deleteSubscription(UUID id) {
    subscriptionRepository.deleteById(id);
  }

  public Optional<Subscription> findSubscriptionById(UUID id) {
    return subscriptionRepository.findById(id);
  }

  public List<Subscription> getActiveSubscriptionsByUserId(UUID userId) {
    return refreshSubscriptionsForUser(userId).stream()
        .filter(sub -> sub.getEndDate().isAfter(LocalDateTime.now()))
        .toList();
  }

  public List<Subscription> getExpiredSubscriptionsByUserId(UUID userId) {
    return getSubscriptionsByUserId(userId).stream()
        .filter(sub -> sub.getEndDate().isBefore(LocalDateTime.now()))
        .toList();
  }

  public List<Subscription> getSubscriptionsByUserId(UUID userId) {
    return refreshSubscriptionsForUser(userId);
  }

  public Subscription updateAutoRenewal(UUID id, boolean autoRenewal) {
    Subscription existingSubscription = subscriptionRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Subscription not found with id: " + id));

    existingSubscription.setAutoRenewal(autoRenewal);
    return subscriptionRepository.save(existingSubscription);
  }

  private Subscription renewIfNeeded(Subscription sub, LocalDateTime now) {
    if (!sub.isAutoRenewal() || sub.getEndDate() == null) {
      return sub;
    }

    LocalDateTime currentEndDate = sub.getEndDate();
    if (!currentEndDate.isBefore(now)) {
      return sub;
    }

    while (currentEndDate.isBefore(now)) {
      currentEndDate = currentEndDate.plusMonths(1);
    }

    sub.setEndDate(currentEndDate);
    return subscriptionRepository.save(sub);
  }

  private List<Subscription> refreshSubscriptionsForUser(UUID userId) {
    LocalDateTime now = LocalDateTime.now();
    return subscriptionRepository.findByUserId(userId).stream()
        .map(sub -> renewIfNeeded(sub, now))
        .toList();
  }
}
