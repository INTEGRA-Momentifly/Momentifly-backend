package integra.momentifly.config.seed;

import integra.momentifly.model.User;

import java.util.List;

public class UserSeedData {
    public static List<User> createUserList(){
        return List.of(
                newUser("Paul", "paul@gmail.com", "paul123", 120, 2285.0),
                newUser("Andrei", "andrei@gmail.com", "andrei123", 200, 2285.0),
                newUser("Andreea", "andreea@gmail.com", "andreea123", 300, 2285.0),
                newUser("David", "david@gmail.com", "david123", 185, 2285.0),
                newUser("Sergiu", "sergiu@gmail.com", "sergiu123", 500, 2285.0),
                newUser("Gabriel", "gabriel@gmail.com", "gabriel123", 98, 2285.0)
        );
    }

    private static User newUser(String username, String email, String password, int score, Double dailyCalories){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setScore(score);
        user.setDailyCalorieGoal(dailyCalories);
        return user;
    }
}
