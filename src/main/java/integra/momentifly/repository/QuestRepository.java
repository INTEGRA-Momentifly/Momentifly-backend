package integra.momentifly.repository;

import integra.momentifly.model.Quest;
import integra.momentifly.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestRepository extends JpaRepository<Quest, UUID> {
  List<Quest> findByUserId(UUID userId);
}
