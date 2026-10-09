package integra.momentifly.config.seed;

import integra.momentifly.model.Subscription;
import integra.momentifly.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SubscriptionSeedData {
    public static List<Subscription> buildTaskList() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setPassword("password");
        user.setEmail("email@gmail.com");
        user.setScore(0);
        user.setDailyCalorieGoal(2000.0);
        user.setUsername("username");

        return List.of(
                newSubscription(user, 15.2, "netflix", LocalDateTime.now()),
                newSubscription(user, 10.2, "crunchyroll", LocalDateTime.now()),
                newSubscription(user, 20.0, "HBO", LocalDateTime.now()),
                newSubscription(user, 10.0, "youtube premium", LocalDateTime.now())
        );
    }

    private static Subscription newSubscription(User user, Double cost, String name, LocalDateTime endDate) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setCost(cost);
        subscription.setName(name);
        subscription.setEndDate(endDate);
        return subscription;
    }
}
