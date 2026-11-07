package integra.momentifly.controller;

import integra.momentifly.dto.CreateQuestRequest;
import integra.momentifly.dto.QuestCompletionResponse;
import integra.momentifly.dto.UpdateQuestRequest;
import integra.momentifly.mapper.QuestCompletionMapper;
import integra.momentifly.model.Quest;
import integra.momentifly.model.QuestCompletion;
import integra.momentifly.model.QuestEnum;
import integra.momentifly.service.QuestCompletionService;
import integra.momentifly.service.QuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestControllerTest {

  @Mock
  private QuestService questService;

  @Mock
  private QuestCompletionService questCompletionService;

  @Mock
  private QuestCompletionMapper mapper;

  @Mock
  private Authentication authentication;

  @InjectMocks
  private QuestController questController;

  private String authName;
  private UUID userId;

  @BeforeEach
  void setUp() {
    authName = "testUser";
    userId = UUID.randomUUID();
    when(authentication.getName()).thenReturn(authName);
  }

  // ==========================================
  // COMPLETE QUEST TESTS
  // ==========================================

  @Test
  void completeQuest_ShouldReturn200AndDto_WhenQuestExistsAndNotCompleted() {
    UUID questId = UUID.randomUUID();
    Quest quest = new Quest(questId, "Task", 15.0, userId);
    QuestCompletion completion = new QuestCompletion();
    QuestCompletionResponse dtoResponse = new QuestCompletionResponse();

    when(questService.getQuestById(questId, authName)).thenReturn(Optional.of(quest));
    when(questCompletionService.completeQuest(eq(questId), eq(authName), eq(15.0), eq(QuestEnum.COMPLETE), any(LocalDateTime.class)))
        .thenReturn(Optional.of(completion));
    when(mapper.toDto(completion)).thenReturn(dtoResponse);

    ResponseEntity<QuestCompletionResponse> response = questController.completeQuest(authentication, questId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(dtoResponse, response.getBody());
    verify(questService).getQuestById(questId, authName);
    verify(questCompletionService).completeQuest(eq(questId), eq(authName), eq(15.0), eq(QuestEnum.COMPLETE), any(LocalDateTime.class));
  }

  @Test
  void completeQuest_ShouldReturn404_WhenQuestDoesNotExist() {
    UUID questId = UUID.randomUUID();
    when(questService.getQuestById(questId, authName)).thenReturn(Optional.empty());

    ResponseEntity<QuestCompletionResponse> response = questController.completeQuest(authentication, questId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(questService).getQuestById(questId, authName);
    verify(questCompletionService, never()).completeQuest(any(), any(), any(), any(), any());
  }

  @Test
  void completeQuest_ShouldReturn400_WhenQuestAlreadyCompleted() {
    UUID questId = UUID.randomUUID();
    Quest quest = new Quest(questId, "Task", 15.0, userId);

    when(questService.getQuestById(questId, authName)).thenReturn(Optional.of(quest));
    when(questCompletionService.completeQuest(eq(questId), eq(authName), eq(15.0), eq(QuestEnum.COMPLETE), any(LocalDateTime.class)))
        .thenReturn(Optional.empty());

    ResponseEntity<QuestCompletionResponse> response = questController.completeQuest(authentication, questId);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    verify(questService).getQuestById(questId, authName);
    verify(questCompletionService).completeQuest(eq(questId), eq(authName), eq(15.0), eq(QuestEnum.COMPLETE), any(LocalDateTime.class));
  }

  // ==========================================
  // WEEK SCORE TESTS
  // ==========================================

  @Test
  void weekScore_ShouldReturnScore() {
    LocalDateTime now = LocalDateTime.now();
    when(questCompletionService.weekScore(now, authName)).thenReturn(85);

    Integer response = questController.weekScore(authentication, now);

    assertNotNull(response);
    assertEquals(85, response);
    verify(questCompletionService).weekScore(now, authName);
  }

  // ==========================================
  // CRUD QUEST TESTS
  // ==========================================

  @Test
  void createQuest_ShouldReturn200AndQuest() {
    CreateQuestRequest request = new CreateQuestRequest("Task", 15.0);
    Quest savedQuest = new Quest(UUID.randomUUID(), "Task", 15.0, userId);

    when(questService.createQuest(eq(authName), any(CreateQuestRequest.class))).thenReturn(savedQuest);

    ResponseEntity<Quest> response = questController.createQuest(authentication, request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(savedQuest, response.getBody());
    verify(questService).createQuest(authName, request);
  }

  @Test
  void getAllQuests_ShouldReturn200AndList() {
    Quest quest = new Quest(UUID.randomUUID(), "Task", 15.0, userId);
    when(questService.getAllQuests(authName)).thenReturn(List.of(quest));

    ResponseEntity<List<Quest>> response = questController.getAllQuests(authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    verify(questService).getAllQuests(authName);
  }

  @Test
  void getQuestById_ShouldReturn200_WhenExists() {
    UUID id = UUID.randomUUID();
    Quest quest = new Quest(id, "Task", 15.0, userId);
    when(questService.getQuestById(id, authName)).thenReturn(Optional.of(quest));

    ResponseEntity<Quest> response = questController.getQuestById(authentication, id);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(quest, response.getBody());
    verify(questService).getQuestById(id, authName);
  }

  @Test
  void getQuestById_ShouldReturn404_WhenNotFound() {
    UUID id = UUID.randomUUID();
    when(questService.getQuestById(id, authName)).thenReturn(Optional.empty());

    ResponseEntity<Quest> response = questController.getQuestById(authentication, id);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(questService).getQuestById(id, authName);
  }

  @Test
  void updateQuest_ShouldReturn200_WhenUpdated() {
    UUID id = UUID.randomUUID();
    UpdateQuestRequest request = new UpdateQuestRequest("Updated", 20.0);
    Quest updatedQuest = new Quest(id, "Updated", 20.0, userId);

    when(questService.updateQuest(eq(id), any(UpdateQuestRequest.class), eq(authName))).thenReturn(updatedQuest);

    ResponseEntity<Quest> response = questController.updateQuest(authentication, id, request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedQuest, response.getBody());
    verify(questService).updateQuest(id, request, authName);
  }

  @Test
  void deleteQuest_ShouldReturn204() {
    UUID id = UUID.randomUUID();

    ResponseEntity<Void> response = questController.deleteQuest(authentication, id);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(questService).deleteQuest(id, authName);
  }
}
