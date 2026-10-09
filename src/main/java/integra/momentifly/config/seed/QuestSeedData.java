package integra.momentifly.config.seed;

import integra.momentifly.model.Quest;
import java.util.List;

public class QuestSeedData {
    public static List<Quest> buildTaskList() {
        return List.of(
                newQuest("Drink a full glass of water within 30 minutes of waking up.", 10),
                newQuest("Walk at least 8,000 steps today.", 20),
                newQuest("Eat at least 3 servings of vegetables today.", 30),
                newQuest("Finish the day within your daily calorie goal.", 40),
                newQuest("Do 30 minutes of any physical activity", 30),
                newQuest("Log all your meals today, including snacks.", 25),
                newQuest("Go the whole day without sugary drinks or desserts.", 30),
                newQuest("Complete at least one quest every day for 7 days in a row.", 100)
        );
    }

    private static Quest newQuest(String description, int points) {
        Quest quest = new Quest();
        quest.setDescription(description);
        quest.setPoints(points);
        return quest;
    }
}
