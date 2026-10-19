package integra.momentifly.dto;

import jakarta.validation.constraints.*;

public record CreateUserRequest(@NotBlank(message = "Username is required")
                                @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
                                @Pattern(regexp = "^[A-Za-z0-9._-]*$",
                                         message = "Username can only contain letters, numbers, dots, underscores and hyphens")
                                String username,

                                @NotBlank(message = "Email is required")
                                @Size(max = 254, message = "Email is too long")
                                String email,

                                @NotBlank(message = "Password is required")
                                @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
                                @Pattern(regexp = "^(?=.*\\p{L})(?=.*\\d).*$", message = "Password must contain at least one letter and one number")
                                String password,

                                @NotNull(message = "Daily calorie goal is required")
                                @Positive(message = "Daily calorie goal must be greater than 0")
                                Double dailyCalorieGoal) {
}
