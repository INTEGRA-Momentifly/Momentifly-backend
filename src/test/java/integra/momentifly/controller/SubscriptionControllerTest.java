package integra.momentifly.controller;

import integra.momentifly.dto.CreateSubscriptionRequest;
import integra.momentifly.dto.UpdateSubscriptionRequest;
import integra.momentifly.exception.NotFoundException;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
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
    private Authentication authentication;

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

        authentication = new UsernamePasswordAuthenticationToken("Vlad", null);
    }

    @Test
    @DisplayName("POST /api/subscriptions creates and returns new subscription")
    void createSubscription_Success() {
        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setName("spotify");
        request.setCost(9.99);
        request.setEndDate(sampleSubscription.getEndDate());

        when(subscriptionService.createSubscription(eq("Vlad"), any(CreateSubscriptionRequest.class)))
                .thenReturn(sampleSubscription);

        Subscription result = subscriptionController.createSubscription(authentication, request);

        assertNotNull(result);
        assertEquals("spotify", result.getName());
        assertEquals(9.99, result.getCost());

        verify(subscriptionService, times(1)).createSubscription(eq("Vlad"), any(CreateSubscriptionRequest.class));
    }

    @Test
    @DisplayName("PUT /api/subscriptions/{id} updates and returns updated subscription")
    void updateSubscription_Success() {
        UUID subscriptionId = UUID.randomUUID();

        UpdateSubscriptionRequest updateRequest = new UpdateSubscriptionRequest();
        updateRequest.setName("spotify-premium");
        updateRequest.setCost(12.99);

        when(subscriptionService.updateSubscription(eq(subscriptionId), eq("Vlad"), any(UpdateSubscriptionRequest.class)))
                .thenReturn(sampleSubscription);

        Subscription result = subscriptionController.updateSubscription(authentication, subscriptionId, updateRequest);

        assertNotNull(result);
        verify(subscriptionService, times(1)).updateSubscription(eq(subscriptionId), eq("Vlad"), any(UpdateSubscriptionRequest.class));
    }

    @Test
    @DisplayName("DELETE subscription returns 204 No Content")
    void deleteSubscription_Success() {
        doNothing().when(subscriptionService).deleteSubscription(subscriptionId, "Vlad");

        ResponseEntity<Void> response = subscriptionController.deleteSubscription(authentication, subscriptionId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(subscriptionService, times(1)).deleteSubscription(subscriptionId, "Vlad");
    }



    @Test
    @DisplayName("GET subscription by ID returns 200 OK with Subscription body when found")
    void getSubscriptionByIdSuccess() {
        //Arrange
        when(subscriptionService.findSubscriptionById(subscriptionId, "Vlad"))
                .thenReturn(sampleSubscription);

        //Act
        ResponseEntity<Subscription> response = subscriptionController.getSubscriptionById(authentication, subscriptionId);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("spotify", response.getBody().getName());
        assertEquals(9.99, response.getBody().getCost());
        assertEquals(userId, response.getBody().getUser().getId());
    }

    @Test
    @DisplayName("GET subscription by ID throws NotFoundException when missing or not owned")
    void getSubscriptionById_NotFound(){
        when(subscriptionService.findSubscriptionById(subscriptionId, "Vlad"))
                .thenThrow(new NotFoundException("Subscription not found"));

        assertThrows(NotFoundException.class,
                () -> subscriptionController.getSubscriptionById(authentication, subscriptionId));
    }

    @Test
    @DisplayName("GET /api/subscriptions returns the logged in user's subscriptions")
    void getAllSubscriptions_ReturnsTheLoggedInUsersSubscriptions() {
        when(subscriptionService.getSubscriptionsForUser("Vlad")).thenReturn(List.of(sampleSubscription));

        List<Subscription> result = subscriptionController.getAllSubscriptions(authentication);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionService, times(1)).getSubscriptionsForUser("Vlad");
    }

    @Test
    @DisplayName("GET /api/subscriptions never asks for another user's subscriptions")
    void getAllSubscriptions_NeverQueriesAnotherUsersSubscriptions() {
        when(subscriptionService.getSubscriptionsForUser("Vlad")).thenReturn(List.of(sampleSubscription));

        List<Subscription> result = subscriptionController.getAllSubscriptions(authentication);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionService, times(1)).getSubscriptionsForUser("Vlad");
        verify(subscriptionService, never()).getSubscriptionsForUser("Andrei");
    }
}