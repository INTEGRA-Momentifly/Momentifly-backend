package integra.momentifly.repository;

import integra.momentifly.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByUser_Username(String username);
    Optional<Task> findByIdAndUser_Username(UUID id, String username);
}
