package integra.momentifly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuestRequest {

    private String description;
    private Double points;
}
