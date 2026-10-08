package integra.momentifly.dto;

public record CreateUserRequest(String username, String email, String password, Double dailyCalorieGoal) {
}
