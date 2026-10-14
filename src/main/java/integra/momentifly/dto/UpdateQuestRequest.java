package integra.momentifly.dto;

import lombok.Getter;
import lombok.Setter;

public record UpdateQuestRequest(
        String description,
        Double points
) {}
