package integra.momentifly.repository;

import integra.momentifly.model.QuestCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestCompletionRepository extends JpaRepository<QuestCompletion, UUID> {

  List<QuestCompletion> findByUserIdAndQuestId(UUID userId,UUID uuid);

  List<QuestCompletion> findByUserIdAndCompletionDateBetween(UUID userId, LocalDateTime start, LocalDateTime end);
}
