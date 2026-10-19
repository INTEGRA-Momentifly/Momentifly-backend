package integra.momentifly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(@Size(max = 254, message = "Email is too long")
                                @NotBlank(message = "Email is required")
                                String email,

                                @NotNull(message = "Daily calorie goal is required")
                                @Positive(message = "Daily calorie goal must be greater than 0")
                                Double dailyCalorieGoal) { }
