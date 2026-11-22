package integra.momentifly.dto;

import java.util.UUID;

public record CreateQuestRequest(
        String description,
        Double points,
        UUID challengeId
) {}
