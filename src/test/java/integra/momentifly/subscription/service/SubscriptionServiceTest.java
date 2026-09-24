package integra.momentifly.subscription.service;

import integra.momentifly.subscription.domain.Subscription;
import integra.momentifly.subscription.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {
    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void createSubscription_Success() {
        Subscription newSubscription = new Subscription();
        newSubscription.setName("Spotify");
        newSubscription.setCost(9.99);

        Subscription savedSubscription = new Subscription();
        savedSubscription.setId(UUID.randomUUID());
        savedSubscription.setName("Spotify");
        savedSubscription.setCost(9.99);

        when(subscriptionRepository.save(newSubscription)).thenReturn(savedSubscription);

        Subscription result = subscriptionService.createSubscription(newSubscription);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Spotify", result.getName());
        assertEquals(9.99, result.getCost());
        verify(subscriptionRepository).save(newSubscription);
    }

    @Test
    void updateSubscription_WhenExists_UpdatesFieldsAndSaves() {
        UUID id = UUID.randomUUID();
        Subscription existing = new Subscription();
        existing.setName("Old Name");

        Subscription updatedInfo = new Subscription();
        updatedInfo.setName("New Name");
        updatedInfo.setCost(12.99);

        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(existing));
        when(subscriptionRepository.save(existing)).thenReturn(existing);

        Subscription result = subscriptionService.updateSubscription(id, updatedInfo);

        // Assert
        assertEquals("New Name", result.getName());
        assertEquals(12.99, result.getCost());
    }

    @Test
    void updateSubscription_WhenNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(subscriptionRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> subscriptionService.updateSubscription(id, new Subscription())
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
    void findSubscriptionById_WhenFound_ReturnsSubscription() {
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
    void findSubscriptionById_WhenNotFound_ReturnsEmptyOptional() {
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
        when(subscriptionRepository.findByUser_Id(userId)).thenReturn(java.util.List.of(subscription));

        java.util.List<Subscription> result = subscriptionService.getSubscriptionsByUserId(userId);

        assertEquals(1, result.size());
        verify(subscriptionRepository).findByUser_Id(userId);
    }
}