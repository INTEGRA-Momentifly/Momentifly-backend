package integra.momentifly.service;

import integra.momentifly.dto.CreateSubscriptionRequest;
import integra.momentifly.dto.UpdateSubscriptionRequest;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.Subscription;
import integra.momentifly.model.User;
import integra.momentifly.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private UserService userService;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void createSubscription_Success() {
        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setName("Spotify");
        request.setCost(9.99);

        User user = new User();
        user.setUsername("Vlad");
        when(userService.getUserByUsername("Vlad")).thenReturn(user);

        Subscription savedSubscription = new Subscription();
        savedSubscription.setId(UUID.randomUUID());
        savedSubscription.setName("Spotify");
        savedSubscription.setCost(9.99);

        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(savedSubscription);

        Subscription result = subscriptionService.createSubscription("Vlad", request);

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

        when(subscriptionRepository.findByIdAndUser_Username(id, "Vlad")).thenReturn(Optional.of(existingSubscription));
        when(subscriptionRepository.save(existingSubscription)).thenReturn(existingSubscription);

        Subscription result = subscriptionService.updateSubscription(id, "Vlad", updateSubscriptionRequest);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals(12.99, result.getCost());
        verify(subscriptionRepository).findByIdAndUser_Username(id, "Vlad");
        verify(subscriptionRepository).save(existingSubscription);
    }

    @Test
    void updateSubscription_WhenNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        UpdateSubscriptionRequest updateSubscriptionRequest = new UpdateSubscriptionRequest();
        updateSubscriptionRequest.setName("New Name");

        when(subscriptionRepository.findByIdAndUser_Username(id, "Vlad")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> subscriptionService.updateSubscription(id, "Vlad", updateSubscriptionRequest)
        );

        assertTrue(exception.getMessage().contains("Subscription not found"));
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void deleteSubscription_Success() {
        UUID id = UUID.randomUUID();
        Subscription subscription = new Subscription();
        subscription.setId(id);

        when(subscriptionRepository.findByIdAndUser_Username(id, "Vlad")).thenReturn(Optional.of(subscription));
        doNothing().when(subscriptionRepository).delete(subscription);

        subscriptionService.deleteSubscription(id, "Vlad");

        verify(subscriptionRepository, times(1)).delete(subscription);
    }

    @Test
    void findSubscriptionById_WhenFound_ReturnsSubscription() {
        UUID id = UUID.randomUUID();
        Subscription subscription = new Subscription();
        subscription.setId(id);

        when(subscriptionRepository.findByIdAndUser_Username(id, "Vlad")).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionService.findSubscriptionById(id, "Vlad");

        assertEquals(id, result.getId());
        verify(subscriptionRepository).findByIdAndUser_Username(id, "Vlad");
    }

    @Test
    void findSubscriptionById_WhenNotFound_ThrowsNotFoundException() {
        UUID id = UUID.randomUUID();
        when(subscriptionRepository.findByIdAndUser_Username(id, "Vlad")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subscriptionService.findSubscriptionById(id, "Vlad"));
        verify(subscriptionRepository).findByIdAndUser_Username(id, "Vlad");
    }

    @Test
    void getAllSubscriptions_Success() {
        Subscription subscription = new Subscription();
        when(subscriptionRepository.findByUser_Username("Vlad")).thenReturn(List.of(subscription));

        List<Subscription> result = subscriptionService.getSubscriptionsForUser("Vlad");

        assertEquals(1, result.size());
        verify(subscriptionRepository).findByUser_Username("Vlad");
    }

    @Test
    void getSubscriptionsByUserId_Success() {
        when(subscriptionRepository.findByUser_Username("Andrei")).thenReturn(List.of());

        List<Subscription> result = subscriptionService.getSubscriptionsForUser("Andrei");

        assertTrue(result.isEmpty());
        verify(subscriptionRepository).findByUser_Username("Andrei");
    }
}