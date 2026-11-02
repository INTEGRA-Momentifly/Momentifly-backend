package integra.momentifly.dto;

import integra.momentifly.model.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record WriteTaskRequest(@NotBlank(message = "Description is required")
                               @Size(max = 255, message = "Description must be at most 255 characters")
                               String description,
                               @NotNull(message = "Due Date is required")
                               LocalDate dueDate,
                               @NotNull(message = "Difficulty is required")
                               Difficulty difficulty) { }
