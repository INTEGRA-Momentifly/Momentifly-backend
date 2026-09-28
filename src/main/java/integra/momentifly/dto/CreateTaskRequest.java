package integra.momentifly.dto;

import integra.momentifly.model.Difficulty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateTaskRequest {
    public UUID userId;
    public String description;
    public LocalDate dueDate;
    public Difficulty difficulty;
}
