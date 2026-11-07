package integra.momentifly.service;

import integra.momentifly.model.*;
import integra.momentifly.repository.QuestCompletionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class QuestCompletionServiceTest {

  @Mock
  private QuestCompletionRepository questCompletionRepository;

  @Mock
  private UserService userService;

  @Mock
  private QuestService questService;

  @InjectMocks
  private QuestCompletionService questCompletionService;

  private String authName;
  private UUID userId;
  private UUID questId;
  private User mockUser;

  @BeforeEach
  void setUp() {
    authName = "testUser";
    userId = UUID.randomUUID();
    questId = UUID.randomUUID();

    mockUser = new User();
    mockUser.setId(userId);
    mockUser.setUsername(authName);
  }

  // ==========================================
  // SCHEDULED JOB TESTS (registerMissedQuestsDaily)
  // ==========================================

  @Test
  void registerMissedQuestsDaily_ShouldSaveOpenCompletion_WhenQuestNotRecorded() {
    // Given
    Quest quest = new Quest(questId, "Daily Walk", 20.0, userId);

    when(userService.findAll()).thenReturn(List.of(mockUser));
    when(questService.findByUserId(userId)).thenReturn(List.of(quest));
    when(questCompletionRepository.findByUserIdAndQuestId(userId, questId)).thenReturn(Collections.emptyList());

    // When
    questCompletionService.registerMissedQuestsDaily();

    // Then
    ArgumentCaptor<QuestCompletion> captor = ArgumentCaptor.forClass(QuestCompletion.class);
    verify(questCompletionRepository, times(1)).save(captor.capture());

    QuestCompletion savedCompletion = captor.getValue();
    assertEquals(questId, savedCompletion.getQuestId());
    assertEquals(userId, savedCompletion.getUserId());
    assertEquals(QuestEnum.OPEN, savedCompletion.getCompletionStatus());
    assertEquals(20.0, savedCompletion.getPoints());
    assertNotNull(savedCompletion.getCompletionDate());
  }

  @Test
  void registerMissedQuestsDaily_ShouldSkip_WhenQuestAlreadyRecorded() {
    // Given
    Quest quest = new Quest(questId, "Daily Walk", 20.0, userId);
    QuestCompletion existingCompletion = new QuestCompletion();

    when(userService.findAll()).thenReturn(List.of(mockUser));
    when(questService.findByUserId(userId)).thenReturn(List.of(quest));
    when(questCompletionRepository.findByUserIdAndQuestId(userId, questId)).thenReturn(List.of(existingCompletion));

    // When
    questCompletionService.registerMissedQuestsDaily();

    // Then
    verify(questCompletionRepository, never()).save(any(QuestCompletion.class));
  }

  @Test
  void registerMissedQuestsDaily_ShouldDoNothing_WhenNoUsersExist() {
    // Given
    when(userService.findAll()).thenReturn(Collections.emptyList());

    // When
    questCompletionService.registerMissedQuestsDaily();

    // Then
    verify(questService, never()).findByUserId(any());
    verify(questCompletionRepository, never()).save(any());
  }

  // ==========================================
  // OTHER SERVICE METHODS TESTS
  // ==========================================

  @Test
  void completeQuest_ShouldSaveAndReturnOptional_WhenNotAlreadyCompleted() {
    // Given
    LocalDateTime now = LocalDateTime.now();
    when(userService.getUserByUsername(authName)).thenReturn(mockUser);
    when(questCompletionRepository.findByUserIdAndQuestId(userId, questId)).thenReturn(Collections.emptyList());
    when(questCompletionRepository.save(any(QuestCompletion.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // When
    Optional<QuestCompletion> result = questCompletionService.completeQuest(
        questId, authName, 15.0, QuestEnum.COMPLETE, now
    );

    // Then
    assertTrue(result.isPresent());
    assertEquals(questId, result.get().getQuestId());
    assertEquals(userId, result.get().getUserId());
    assertEquals(QuestEnum.COMPLETE, result.get().getCompletionStatus());
    assertEquals(15.0, result.get().getPoints());
    verify(questCompletionRepository, times(1)).save(any(QuestCompletion.class));
  }

  @Test
  void completeQuest_ShouldReturnEmptyOptional_WhenAlreadyCompleted() {
    // Given
    when(userService.getUserByUsername(authName)).thenReturn(mockUser);
    when(questCompletionRepository.findByUserIdAndQuestId(userId, questId)).thenReturn(List.of(new QuestCompletion()));

    // When
    Optional<QuestCompletion> result = questCompletionService.completeQuest(
        questId, authName, 15.0, QuestEnum.COMPLETE, LocalDateTime.now()
    );

    // Then
    assertTrue(result.isEmpty());
    verify(questCompletionRepository, never()).save(any());
  }

  @Test
  void weekScore_ShouldCalculateCorrectPercentage() {
    // Given
    LocalDateTime now = LocalDateTime.now();
    QuestCompletion c1 = new QuestCompletion();
    c1.setCompletionStatus(QuestEnum.COMPLETE);
    c1.setPoints(30.0);

    QuestCompletion c2 = new QuestCompletion();
    c2.setCompletionStatus(QuestEnum.OPEN);
    c2.setPoints(10.0);

    when(userService.getUserByUsername(authName)).thenReturn(mockUser);
    when(questCompletionRepository.findByUserIdAndCompletionDateBetween(eq(userId), any(), eq(now)))
        .thenReturn(List.of(c1, c2));

    // When (30 / 40 = 75%)
    Integer score = questCompletionService.weekScore(now, authName);

    // Then
    assertEquals(75, score);
  }

  @Test
  void weekScore_ShouldReturnZero_WhenTotalPointsIsZero() {
    // Given
    LocalDateTime now = LocalDateTime.now();
    when(userService.getUserByUsername(authName)).thenReturn(mockUser);
    when(questCompletionRepository.findByUserIdAndCompletionDateBetween(eq(userId), any(), eq(now)))
        .thenReturn(Collections.emptyList());

    // When
    Integer score = questCompletionService.weekScore(now, authName);

    // Then
    assertEquals(0, score);
  }

  @Test
  void findByUserIdAndQuestId_ShouldReturnList() {
    when(questCompletionRepository.findByUserIdAndQuestId(userId, questId))
        .thenReturn(List.of(new QuestCompletion()));

    List<QuestCompletion> result = questCompletionService.findByUserIdAndQuestId(userId, questId);

    assertEquals(1, result.size());
    verify(questCompletionRepository).findByUserIdAndQuestId(userId, questId);
  }

  @Test
  void findAll_ShouldReturnAllCompletions() {
    when(questCompletionRepository.findAll()).thenReturn(List.of(new QuestCompletion()));

    List<QuestCompletion> result = questCompletionService.findAll();

    assertEquals(1, result.size());
    verify(questCompletionRepository).findAll();
  }

  @Test
  void findById_ShouldReturnOptional() {
    UUID id = UUID.randomUUID();
    when(questCompletionRepository.findById(id)).thenReturn(Optional.of(new QuestCompletion()));

    Optional<QuestCompletion> result = questCompletionService.findById(id);

    assertTrue(result.isPresent());
    verify(questCompletionRepository).findById(id);
  }

  @Test
  void save_ShouldCallRepositorySave() {
    QuestCompletion completion = new QuestCompletion();
    when(questCompletionRepository.save(completion)).thenReturn(completion);

    QuestCompletion result = questCompletionService.save(completion);

    assertNotNull(result);
    verify(questCompletionRepository).save(completion);
  }

  @Test
  void deleteById_ShouldCallRepositoryDelete() {
    UUID id = UUID.randomUUID();

    questCompletionService.deleteById(id);

    verify(questCompletionRepository).deleteById(id);
  }
}
