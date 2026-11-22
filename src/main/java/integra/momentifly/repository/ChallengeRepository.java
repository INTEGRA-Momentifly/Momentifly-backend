package integra.momentifly.repository;

import integra.momentifly.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {
}
