package integra.momentifly.dto;

import lombok.Getter;
import lombok.Setter;

public record CreateQuestRequest(
        String description,
        Double points
) {}
