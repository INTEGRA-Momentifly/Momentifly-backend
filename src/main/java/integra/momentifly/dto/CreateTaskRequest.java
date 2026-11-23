package integra.momentifly.dto;

import integra.momentifly.model.Difficulty;

import java.time.LocalDate;

public record CreateTaskRequest (String description, LocalDate dueDate, Difficulty difficulty) { }
