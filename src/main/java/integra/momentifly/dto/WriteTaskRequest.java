package integra.momentifly.dto;

import integra.momentifly.model.Difficulty;
import java.time.LocalDate;
import java.util.UUID;

public record WriteTaskRequest(String description,LocalDate dueDate,Difficulty difficulty) {}
