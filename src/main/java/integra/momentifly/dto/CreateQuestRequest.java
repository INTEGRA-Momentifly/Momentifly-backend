package integra.momentifly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateQuestRequest(
        @NotBlank(message = "Description is required")
        String description,
        @NotNull(message = "Points are required")
        @Positive(message = "Points must be greater than 0")
        Double points,
        UUID challengeId
) {}
