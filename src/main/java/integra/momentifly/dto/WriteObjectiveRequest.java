package integra.momentifly.dto;

import integra.momentifly.model.MacroType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record WriteObjectiveRequest(
        @NotNull(message = "Type is required") MacroType type,
        @NotNull(message = "Value is required")
        @Positive(message = "Value must be greater than 0") Integer value) {}