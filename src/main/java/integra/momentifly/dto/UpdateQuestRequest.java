package integra.momentifly.dto;

import java.util.UUID;

public record UpdateQuestRequest(
        String description,
        Double points,
        UUID challengeId
) {}
