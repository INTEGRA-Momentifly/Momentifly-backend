package integra.momentifly.service;

import integra.momentifly.dto.CreateSubscriptionRequest;
import integra.momentifly.dto.UpdateSubscriptionRequest;
import integra.momentifly.model.Subscription;
import integra.momentifly.model.User;
import integra.momentifly.repository.SubscriptionRepository;
import integra.momentifly.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {
  @Mock
  private SubscriptionRepository subscriptionRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private SubscriptionService subscriptionService;

  @Test
  void createSubscription_Success() {
    CreateSubscriptionRequest request = new CreateSubscriptionRequest();
    request.setUserId(UUID.randomUUID());
    request.setName("Spotify");
    request.setCost(9.99);

    Subscription savedSubscription = new Subscription();
    savedSubscription.setId(UUID.randomUUID());
    savedSubscription.setName("Spotify");
    savedSubscription.setCost(9.99);

    UUID userId = UUID.randomUUID();
    request.setUserId(userId);

    User user = new User();
    user.setId(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(subscriptionRepository.save(any(Subscription.class))).thenReturn(savedSubscription);

    Subscription result = subscriptionService.createSubscription(request);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals("Spotify", result.getName());
    assertEquals(9.99, result.getCost());
    verify(subscriptionRepository).save(any(Subscription.class));
  }

  @Test
  void updateSubscription_WhenExists_UpdatesFieldsAndSaves() {
    UUID id = UUID.randomUUID();

    Subscription existingSubscription = new Subscription();
    existingSubscription.setId(id);
    existingSubscription.setName("Old Name");
    existingSubscription.setCost(9.99);

    UpdateSubscriptionRequest updateSubscriptionRequest = new UpdateSubscriptionRequest();
    updateSubscriptionRequest.setName("New Name");
    updateSubscriptionRequest.setCost(12.99);

    when(subscriptionRepository.findById(id)).thenReturn(Optional.of(existingSubscription));
    when(subscriptionRepository.save(existingSubscription)).thenReturn(existingSubscription);

    Subscription result = subscriptionService.updateSubscription(id, updateSubscriptionRequest);

    assertNotNull(result);
    assertEquals("New Name", result.getName());
    assertEquals(12.99, result.getCost());
    verify(subscriptionRepository).findById(id);
    verify(subscriptionRepository).save(existingSubscription);
  }

  @Test
  void updateSubscription_WhenNotFound_ThrowsException() {
    UUID id = UUID.randomUUID();
    UpdateSubscriptionRequest updateSubscriptionRequest = new UpdateSubscriptionRequest();
    updateSubscriptionRequest.setName("New Name");

    when(subscriptionRepository.findById(id)).thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(
        RuntimeException.class,
        () -> subscriptionService.updateSubscription(id, updateSubscriptionRequest)
    );

    assertTrue(exception.getMessage().contains("Subscription not found"));
    verify(subscriptionRepository, never()).save(any());
  }

  @Test
  void deleteSubscription_Success() {
    UUID id = UUID.randomUUID();
    doNothing().when(subscriptionRepository).deleteById(id);

    subscriptionService.deleteSubscription(id);

    verify(subscriptionRepository, times(1)).deleteById(id);
  }

  @Test
  void getAllActiveSubscriptions_ReturnsOnlyFutureEndDatesForUser() {
    UUID userId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();

    Subscription active = new Subscription();
    active.setEndDate(now.plusDays(10));

    Subscription expired = new Subscription();
    expired.setEndDate(now.minusDays(2));

    when(subscriptionRepository.findByUserId(userId)).thenReturn(List.of(active, expired));

    List<Subscription> result = subscriptionService.getActiveSubscriptionsByUserId(userId);

    assertEquals(1, result.size());
    assertEquals(active, result.get(0));
  }

  @Test
  void getAllExpiredSubscriptions_ReturnsOnlyPastEndDatesForUser() {
    UUID userId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();

    Subscription active = new Subscription();
    active.setEndDate(now.plusDays(10));

    Subscription expired = new Subscription();
    expired.setEndDate(now.minusDays(2));

    when(subscriptionRepository.findByUserId(userId)).thenReturn(List.of(active, expired));

    List<Subscription> result = subscriptionService.getExpiredSubscriptionsByUserId(userId);

    assertEquals(1, result.size());
    assertEquals(expired, result.getFirst());
  }

  @Test
  void getSubscriptionById_WhenFound_ReturnsSubscription() {
    UUID id = UUID.randomUUID();
    Subscription subscription = new Subscription();
    subscription.setId(id);

    when(subscriptionRepository.findById(id)).thenReturn(Optional.of(subscription));

    Optional<Subscription> result = subscriptionService.findSubscriptionById(id);

    assertTrue(result.isPresent());
    assertEquals(id, result.get().getId());
    verify(subscriptionRepository).findById(id);
  }

  @Test
  void getSubscriptionById_WhenNotFound_ReturnsEmptyOptional() {
    UUID id = UUID.randomUUID();
    when(subscriptionRepository.findById(id)).thenReturn(Optional.empty());

    Optional<Subscription> result = subscriptionService.findSubscriptionById(id);

    assertTrue(result.isEmpty());
    verify(subscriptionRepository).findById(id);
  }

  @Test
  void getAllSubscriptions_Success() {
    Subscription subscription = new Subscription();
    when(subscriptionRepository.findAll()).thenReturn(java.util.List.of(subscription));

    java.util.List<Subscription> result = subscriptionService.getAllSubscriptions();

    assertEquals(1, result.size());
    verify(subscriptionRepository).findAll();
  }

  @Test
  void getSubscriptionsByUserId_Success() {
    UUID userId = UUID.randomUUID();
    Subscription subscription = new Subscription();
    when(subscriptionRepository.findByUserId(userId)).thenReturn(java.util.List.of(subscription));

    java.util.List<Subscription> result = subscriptionService.getSubscriptionsByUserId(userId);

    assertEquals(1, result.size());
    verify(subscriptionRepository).findByUserId(userId);
  }

  @Test
  void updateAutoRenewal_WhenExists_UpdatedFlagAndSets(){
    UUID id = UUID.randomUUID();
    Subscription existing = new Subscription();
    existing.setId(id);
    existing.setAutoRenewal(false);

    when(subscriptionRepository.findById(id)).thenReturn(Optional.of(existing));
    when(subscriptionRepository.save(existing)).thenReturn(existing);

    Subscription result = subscriptionService.updateAutoRenewal(id, true);

    assertTrue(result.isAutoRenewal());
    verify(subscriptionRepository).save(existing);
  }
}
