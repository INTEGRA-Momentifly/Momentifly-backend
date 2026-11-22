package integra.momentifly.dto;

import integra.momentifly.model.ChallengeStatus;
import integra.momentifly.model.Quest;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ChallengeResponse(
        UUID id,
        String name,
        int durationDays,
        LocalDate startDate,
        ChallengeStatus status,
        List<Quest> quests
) {}
