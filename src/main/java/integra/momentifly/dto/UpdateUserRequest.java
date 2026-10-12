package integra.momentifly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateUserRequest(@NotBlank(message = "Username is required") String username,
                                @NotBlank(message = "Email is required")  String email,
                                @NotNull(message = "Daily calorie goal is required")
                                @Positive(message = "Daily calorie goal must be greater than 0")
                                Double dailyCalorieGoal) { }
