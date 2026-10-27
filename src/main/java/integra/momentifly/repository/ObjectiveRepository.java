package integra.momentifly.repository;

import integra.momentifly.model.MacroType;
import integra.momentifly.model.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObjectiveRepository extends JpaRepository<Objective, UUID> {
    List<Objective> findByUserId(UUID userId);
    Optional<Objective> findByUserIdAndType(UUID userId, MacroType type);
    boolean existsByUserIdAndType(UUID userId, MacroType type);
}
