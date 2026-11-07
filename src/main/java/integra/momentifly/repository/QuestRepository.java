package integra.momentifly.repository;

import integra.momentifly.model.Quest;
import integra.momentifly.model.Reminder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestRepository extends JpaRepository<Quest, UUID> {
  List<Quest> findByUserId(UUID userId);

  Optional<Quest> findByIdAndUserId(UUID id, UUID userId);

  @Transactional
  void deleteByIdAndUserId(UUID id, UUID userId);
}
