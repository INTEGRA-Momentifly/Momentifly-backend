package integra.momentifly.repository;

import integra.momentifly.model.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, UUID> {
}
