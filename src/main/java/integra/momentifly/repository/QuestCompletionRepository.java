package integra.momentifly.repository;

import integra.momentifly.model.QuestCompletion;
import integra.momentifly.model.QuestEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestCompletionRepository extends JpaRepository<QuestCompletion, UUID> {

  List<QuestCompletion> findByUserIdAndQuestId(UUID userId,UUID uuid);
  @Query("""
    SELECT DISTINCT qc.questId
    FROM QuestCompletion qc
    WHERE qc.userId = :userId
    AND qc.completionStatus = :status
""")
  List<UUID> findCompletedQuestIds(
      @Param("userId") UUID userId,
      @Param("status") QuestEnum status
  );
  List<QuestCompletion> findByUserIdAndCompletionDateBetween(UUID userId, LocalDateTime start, LocalDateTime end);
}
