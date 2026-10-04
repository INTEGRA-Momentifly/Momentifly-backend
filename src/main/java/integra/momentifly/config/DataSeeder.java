package integra.momentifly.config;

import integra.momentifly.config.seed.TaskSeedData;
import integra.momentifly.model.*;
import integra.momentifly.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final TaskRepository taskRepository;

    public DataSeeder(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) {
        if (taskRepository.count() > 0) return;   // if there is data already then skip
        var taskList = TaskSeedData.buildTaskList();
        taskRepository.saveAll(taskList);
    }
}
