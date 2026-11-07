package integra.momentifly.service;

import integra.momentifly.dto.CreateQuestRequest;
import integra.momentifly.dto.UpdateQuestRequest;
import integra.momentifly.model.Quest;
import integra.momentifly.model.User;
import integra.momentifly.repository.QuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestServiceTest {

  @Mock
  private QuestRepository questRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private QuestService questService;

  private String authName;
  private UUID userId;
  private User mockUser;

  @BeforeEach
  void setUp() {
    authName = "testUser";
    userId = UUID.randomUUID();
    mockUser = new User();
    mockUser.setId(userId);
    mockUser.setUsername(authName);
  }

  @Test
  void createQuest_ShouldReturnSavedQuest() {
    CreateQuestRequest request = new CreateQuestRequest("Test Quest", 10.0);
    Quest savedQuest = new Quest(UUID.randomUUID(), "Test Quest", 10.0, userId);

    when(userService.getUserByUsername(authName)).thenReturn(mockUser);
    when(questRepository.save(any(Quest.class))).thenReturn(savedQuest);

    Quest result = questService.createQuest(authName, request);

    assertNotNull(result.getId());
    assertEquals("Test Quest", result.getDescription());
    assertEquals(userId, result.getUserId());

    verify(userService, times(1)).getUserByUsername(authName);
    verify(questRepository, times(1)).save(any(Quest.class));
  }

  @Test
  void getAllQuests_ShouldReturnListForUser() {
    Quest quest = new Quest(UUID.randomUUID(), "Test", 10.0, userId);

    when(userService.getUserByUsername(authName)).thenReturn(mockUser);
    when(questRepository.findByUserId(userId)).thenReturn(List.of(quest));

    List<Quest> result = questService.getAllQuests(authName);

    assertEquals(1, result.size());
    verify(userService, times(1)).getUserByUsername(authName);
    verify(questRepository, times(1)).findByUserId(userId);
  }

  @Test
  void getQuestById_ShouldReturnQuest_WhenExists() {
    UUID id = UUID.randomUUID();
    Quest quest = new Quest(id, "Test", 10.0, userId);

    when(userService.getUserByUsername(authName)).thenReturn(mockUser);
    when(questRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(quest));

    Optional<Quest> result = questService.getQuestById(id, authName);

    assertTrue(result.isPresent());
    assertEquals(id, result.get().getId());
    assertEquals(userId, result.get().getUserId());
  }

  @Test
  void updateQuest_ShouldUpdateAndReturnQuest_WhenExists() {
    UUID id = UUID.randomUUID();
    Quest existingQuest = new Quest(id, "Old", 10.0, userId);
    UpdateQuestRequest request = new UpdateQuestRequest("New", 20.0);

    when(userService.getUserByUsername(authName)).thenReturn(mockUser);
    when(questRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(existingQuest));
    when(questRepository.save(any(Quest.class))).thenReturn(existingQuest);

    Quest result = questService.updateQuest(id, request, authName);

    assertEquals("New", result.getDescription());
    assertEquals(20.0, result.getPoints());
    verify(questRepository, times(1)).save(any(Quest.class));
  }

  @Test
  void updateQuest_ShouldThrowException_WhenNotFound() {
    UUID id = UUID.randomUUID();
    UpdateQuestRequest request = new UpdateQuestRequest("New", 20.0);

    when(userService.getUserByUsername(authName)).thenReturn(mockUser);
    when(questRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> questService.updateQuest(id, request, authName));
  }

  @Test
  void deleteQuest_ShouldCallRepositoryDelete() {
    UUID id = UUID.randomUUID();

    when(userService.getUserByUsername(authName)).thenReturn(mockUser);

    questService.deleteQuest(id, authName);

    verify(userService, times(1)).getUserByUsername(authName);
    verify(questRepository, times(1)).deleteByIdAndUserId(id, userId);
  }
}
