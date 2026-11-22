package integra.momentifly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ChallengeQuestRequest(
        @NotBlank(message = "Description is required")
        @Size(max = 255, message = "Description must be at most 255 characters")
        String description,
        @NotNull(message = "Points must not be null")
        @Positive(message = "Points must be greater than 0")
        Double points
) {}
