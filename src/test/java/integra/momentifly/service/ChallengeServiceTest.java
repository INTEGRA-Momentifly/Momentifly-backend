package integra.momentifly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import integra.momentifly.dto.ChallengeQuestRequest;
import integra.momentifly.dto.ChallengeResponse;
import integra.momentifly.dto.CreateChallengeRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.model.Challenge;
import integra.momentifly.model.ChallengeParticipant;
import integra.momentifly.model.ChallengeStatus;
import integra.momentifly.model.User;
import integra.momentifly.repository.ChallengeParticipantRepository;
import integra.momentifly.repository.ChallengeRepository;
import integra.momentifly.repository.QuestRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

  @Mock
  private ChallengeRepository challengeRepository;

  @Mock
  private ChallengeParticipantRepository challengeParticipantRepository;

  @Mock
  private QuestRepository questRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private ChallengeService challengeService;

  private CreateChallengeRequest requestWithDuration(int durationDays) {
    return new CreateChallengeRequest(
        "Healthy week",
        durationDays,
        List.of(
            new ChallengeQuestRequest("Drink water", 10.0),
            new ChallengeQuestRequest("Walk 10000 steps", 20.0)));
  }

  @Test
  void createChallenge_ShouldSaveChallengeQuestsAndCreatorAsParticipant() {
    User creator = new User();
    creator.setUsername("david");

    when(userService.getUserByUsername("david")).thenReturn(creator);
    when(challengeRepository.save(any(Challenge.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(questRepository.saveAll(anyList()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ChallengeResponse response = challengeService.createChallenge("david", requestWithDuration(7));

    assertEquals("Healthy week", response.name());
    assertEquals(7, response.durationDays());
    assertEquals(LocalDate.now().plusDays(1), response.startDate());
    assertEquals(ChallengeStatus.UPCOMING, response.status());
    assertEquals(2, response.quests().size());
    assertEquals("Drink water", response.quests().get(0).getDescription());
    assertEquals(10.0, response.quests().get(0).getPoints());
    assertNotNull(response.quests().get(0).getChallenge());

    ArgumentCaptor<Challenge> challengeCaptor = ArgumentCaptor.forClass(Challenge.class);
    verify(challengeRepository).save(challengeCaptor.capture());
    assertSame(creator, challengeCaptor.getValue().getCreator());

    ArgumentCaptor<ChallengeParticipant> participantCaptor =
        ArgumentCaptor.forClass(ChallengeParticipant.class);
    verify(challengeParticipantRepository).save(participantCaptor.capture());
    assertSame(creator, participantCaptor.getValue().getUser());
    assertSame(challengeCaptor.getValue(), participantCaptor.getValue().getChallenge());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 5, 30})
  void createChallenge_ShouldThrow_WhenDurationIsNotAllowed(int durationDays) {
    assertThrows(
        InvalidDataException.class,
        () -> challengeService.createChallenge("david", requestWithDuration(durationDays)));

    verifyNoInteractions(
        userService, challengeRepository, challengeParticipantRepository, questRepository);
  }

  @ParameterizedTest
  @ValueSource(ints = {3, 7, 14})
  void createChallenge_ShouldAccept_WhenDurationIsAllowed(int durationDays) {
    when(userService.getUserByUsername("david")).thenReturn(new User());
    when(challengeRepository.save(any(Challenge.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(questRepository.saveAll(anyList()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ChallengeResponse response =
        challengeService.createChallenge("david", requestWithDuration(durationDays));

    assertEquals(durationDays, response.durationDays());
  }
}
