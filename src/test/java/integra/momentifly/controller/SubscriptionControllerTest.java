package integra.momentifly.controller;

import integra.momentifly.dto.CreateSubscriptionRequest;
import integra.momentifly.dto.UpdateSubscriptionRequest;
import integra.momentifly.model.Subscription;
import integra.momentifly.service.SubscriptionService;
import integra.momentifly.model.User;
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
import java.util.List;
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
        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setUserId(userId);
        request.setName("spotify");
        request.setCost(9.99);
        request.setEndDate(sampleSubscription.getEndDate());

        when(subscriptionService.createSubscription(any(CreateSubscriptionRequest.class)))
                .thenReturn(sampleSubscription);

        Subscription result = subscriptionController.createSubscription(request);

        assertNotNull(result);
        assertEquals("spotify", result.getName());
        assertEquals(9.99, result.getCost());

        verify(subscriptionService, times(1)).createSubscription(any(CreateSubscriptionRequest.class));
    }

    @Test
    @DisplayName("PUT /api/subscriptions/{id} updates and returns updated subscription")
    void updateSubscription_Success() {
        UUID subscriptionId = UUID.randomUUID();

        UpdateSubscriptionRequest updateRequest = new UpdateSubscriptionRequest();
        updateRequest.setName("spotify-premium");
        updateRequest.setCost(12.99);

        when(subscriptionService.updateSubscription(eq(subscriptionId), any(UpdateSubscriptionRequest.class)))
                .thenReturn(sampleSubscription);

        Subscription result = subscriptionController.updateSubscription(subscriptionId, updateRequest);

        assertNotNull(result);
        verify(subscriptionService, times(1)).updateSubscription(eq(subscriptionId), any(UpdateSubscriptionRequest.class));
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
    void getSubscriptionByIdSuccess() {
        //Arrange
        when(subscriptionService.findSubscriptionById(subscriptionId))
                .thenReturn(Optional.of(sampleSubscription));

        //Act
        ResponseEntity<Subscription> response = subscriptionController.getSubscriptionById(subscriptionId);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("spotify", response.getBody().getName());
        assertEquals(9.99, response.getBody().getCost());
        assertEquals(userId, response.getBody().getUser().getId());
    }

    @Test
    @DisplayName("GET subscription by ID returns 404 Not Found when missing")
    void getSubscriptionById_NotFound(){
        when(subscriptionService.findSubscriptionById(subscriptionId))
                .thenReturn(Optional.empty());

        ResponseEntity<Subscription> response = subscriptionController.getSubscriptionById(subscriptionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("GET /api/subscriptions without query param returns all subscriptions")
    void getAllSubscriptions_WithoutUserId_ReturnsAll() {
        when(subscriptionService.getAllSubscriptions()).thenReturn(List.of(sampleSubscription));

        List<Subscription> result = subscriptionController.getAllSubscriptions(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionService, times(1)).getAllSubscriptions();
        verify(subscriptionService, never()).getSubscriptionsByUserId(any());
    }

    @Test
    @DisplayName("GET /api/subscriptions?userId=... returns filtered subscriptions for user")
    void getAllSubscriptions_WithUserId_ReturnsFiltered() {
        when(subscriptionService.getSubscriptionsByUserId(userId)).thenReturn(List.of(sampleSubscription));

        List<Subscription> result = subscriptionController.getAllSubscriptions(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionService, times(1)).getSubscriptionsByUserId(userId);
        verify(subscriptionService, never()).getAllSubscriptions();
    }
}