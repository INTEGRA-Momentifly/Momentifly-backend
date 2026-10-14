package integra.momentifly.repository;

import integra.momentifly.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    List<Subscription> findByUserId(UUID userId);

}
