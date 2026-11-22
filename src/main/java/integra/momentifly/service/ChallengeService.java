package integra.momentifly.service;

import integra.momentifly.dto.ChallengeQuestRequest;
import integra.momentifly.dto.ChallengeResponse;
import integra.momentifly.dto.CreateChallengeRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.model.Challenge;
import integra.momentifly.model.ChallengeParticipant;
import integra.momentifly.model.ChallengeStatus;
import integra.momentifly.model.Quest;
import integra.momentifly.model.User;
import integra.momentifly.repository.ChallengeParticipantRepository;
import integra.momentifly.repository.ChallengeRepository;
import integra.momentifly.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChallengeService {
  private static final Set<Integer> ALLOWED_DURATIONS = Set.of(3, 7, 14);

  private final ChallengeRepository challengeRepository;
  private final ChallengeParticipantRepository challengeParticipantRepository;
  private final QuestRepository questRepository;
  private final UserService userService;

  @Transactional
  public ChallengeResponse createChallenge(String username, CreateChallengeRequest request) {
    if (!ALLOWED_DURATIONS.contains(request.durationDays())) {
      throw new InvalidDataException("Duration must be 3, 7 or 14 days");
    }

    User creator = userService.getUserByUsername(username);

    Challenge challenge = new Challenge();
    challenge.setCreator(creator);
    challenge.setName(request.name());
    challenge.setDurationDays(request.durationDays());
    challenge.setStartDate(LocalDate.now().plusDays(1));
    challenge = challengeRepository.save(challenge);

    List<Quest> quests = new ArrayList<>();
    for (ChallengeQuestRequest questRequest : request.quests()) {
      Quest quest = new Quest();
      quest.setDescription(questRequest.description());
      quest.setPoints(questRequest.points());
      quest.setChallenge(challenge);
      quests.add(quest);
    }
    quests = questRepository.saveAll(quests);

    ChallengeParticipant participant = new ChallengeParticipant();
    participant.setChallenge(challenge);
    participant.setUser(creator);
    challengeParticipantRepository.save(participant);

    return toResponse(challenge, quests);
  }

  private ChallengeResponse toResponse(Challenge challenge, List<Quest> quests) {
    return new ChallengeResponse(
        challenge.getId(),
        challenge.getName(),
        challenge.getDurationDays(),
        challenge.getStartDate(),
        computeStatus(challenge.getStartDate(), challenge.getDurationDays()),
        quests
    );
  }

  // startDate is the first day; the last day is startDate + durationDays - 1.
  private ChallengeStatus computeStatus(LocalDate startDate, int durationDays) {
    LocalDate today = LocalDate.now();
    LocalDate endExclusive = startDate.plusDays(durationDays);

    if (today.isBefore(startDate)) {
      return ChallengeStatus.UPCOMING;
    }
    if (today.isBefore(endExclusive)) {
      return ChallengeStatus.ACTIVE;
    }
    return ChallengeStatus.FINISHED;
  }
}
