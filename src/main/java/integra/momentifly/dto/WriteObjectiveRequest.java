package integra.momentifly.dto;

import integra.momentifly.model.MacroType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record WriteObjectiveRequest(
        @NotNull (message = "User id is required.")
        UUID userId,
        @NotNull (message = "Invalid macro type.")
        MacroType macroType,
        @NotNull @Positive(message="Invalid value. Must be positive.")
        Integer value
) { }