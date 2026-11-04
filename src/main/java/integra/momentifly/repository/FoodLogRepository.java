package integra.momentifly.repository;

import integra.momentifly.model.FoodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface FoodLogRepository extends JpaRepository<FoodLog, UUID> {
}
