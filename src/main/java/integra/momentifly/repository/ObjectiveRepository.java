package integra.momentifly.repository;

import integra.momentifly.model.MacroType;
import integra.momentifly.model.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObjectiveRepository extends JpaRepository<Objective, UUID> {
    List<Objective> findByUser_Username(String username);
    Optional<Objective> findByUser_UsernameAndType(String username, MacroType type);
    boolean existsByUser_UsernameAndType(String username, MacroType type);
}
