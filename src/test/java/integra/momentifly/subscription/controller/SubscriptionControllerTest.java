package integra.momentifly.subscription.controller;

import integra.momentifly.subscription.domain.Subscription;
import integra.momentifly.subscription.service.SubscriptionService;
import integra.momentifly.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest {
    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    private UUID subscriptionId;
    private UUID userId;
    private Subscription sampleSubscription;

    @BeforeEach
    void setUp(){
        subscriptionId = UUID.randomUUID();
        userId = UUID.randomUUID();

        User sampleUser = new User();
        sampleUser.setId(userId);
        sampleUser.setUsername("Vlad");
        sampleUser.setEmail("vlad@example.com");

        sampleSubscription=new Subscription();
        sampleSubscription.setId(subscriptionId);
        sampleSubscription.setUser(sampleUser);
        sampleSubscription.setName("spotify");
        sampleSubscription.setCost(9.99);
        sampleSubscription.setEndDate(LocalDateTime.now().plusMonths(1));
    }

    @Test
    @DisplayName("POST /api/subscriptions creates and returns new subscription")
    void createSubscription_Success() {
        when(subscriptionService.createSubscription(any(Subscription.class))).thenReturn(sampleSubscription);

        Subscription result = subscriptionController.createSubscription(sampleSubscription);

        assertNotNull(result);
        assertEquals("spotify", result.getName());
        verify(subscriptionService, times(1)).createSubscription(sampleSubscription);
    }

    @Test
    @DisplayName("PUT /api/subscriptions/{id} updates and returns subscription")
    void updateSubscription_Success() {
        when(subscriptionService.updateSubscription(eq(subscriptionId), any(Subscription.class))).thenReturn(sampleSubscription);

        Subscription result = subscriptionController.updateSubscription(subscriptionId, sampleSubscription);

        assertNotNull(result);
        assertEquals("spotify", result.getName());
        verify(subscriptionService, times(1)).updateSubscription(subscriptionId, sampleSubscription);
    }

    @Test
    @DisplayName("DELETE subscription returns 204 No Content")
    void deleteSubscription_Success() {
        doNothing().when(subscriptionService).deleteSubscription(subscriptionId);

        ResponseEntity<Void> response = subscriptionController.deleteSubscription(subscriptionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(subscriptionService, times(1)).deleteSubscription(subscriptionId);
    }



    @Test
    @DisplayName("GET subscription by ID returns 200 OK with Subscription body when found")
    void findSubscriptionByIdSuccess() {
        //Arrange
        when(subscriptionService.findSubscriptionById(subscriptionId))
                .thenReturn(Optional.of(sampleSubscription));

        //Act
        ResponseEntity<Subscription> response = subscriptionController.findSubscriptionById(subscriptionId);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("spotify", response.getBody().getName());
        assertEquals(9.99, response.getBody().getCost());
        assertEquals(userId, response.getBody().getUser().getId());
    }

    @Test
    @DisplayName("GET subscription by ID returns 404 Not Found when missing")
    void findSubscriptionById_NotFound(){
        when(subscriptionService.findSubscriptionById(subscriptionId))
                .thenReturn(Optional.empty());

        ResponseEntity<Subscription> response = subscriptionController.findSubscriptionById(subscriptionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("GET /api/subscriptions returns all subscriptions")
    void getAllSubscriptions_Success() {
        when(subscriptionService.getAllSubscriptions()).thenReturn(java.util.List.of(sampleSubscription));

        java.util.List<Subscription> result = subscriptionController.getAllSubscriptions();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionService, times(1)).getAllSubscriptions();
    }

    @Test
    @DisplayName("GET /api/subscriptions/user/{userId} returns user subscriptions")
    void getSubscriptionsByUserId_Success() {
        when(subscriptionService.getSubscriptionsByUserId(userId)).thenReturn(java.util.List.of(sampleSubscription));

        java.util.List<Subscription> result = subscriptionController.getSubscriptionsByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionService, times(1)).getSubscriptionsByUserId(userId);
    }
}