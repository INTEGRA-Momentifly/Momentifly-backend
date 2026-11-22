package integra.momentifly.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateChallengeRequest(
        @NotBlank(message = "Every challenge must have a name.")
        @Size(max = 100, message = "The length of the name of a challenge must be at most 100 characters")
        String name,
        @NotNull(message = "The duration of a challenge must not be null")
        @Positive(message = "The duration of a challenge must be a positive number")
        Integer durationDays,
        @Size(min = 1, max = 5, message = "The number of allowed quests in a challenge is between 1 and 5")
        @NotNull(message = "Must have at least a quest in a challenge")
        @Valid
        List<ChallengeQuestRequest> quests
) {}
