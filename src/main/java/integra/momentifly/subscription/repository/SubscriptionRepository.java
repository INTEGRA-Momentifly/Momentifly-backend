package integra.momentifly.subscription.repository;

import integra.momentifly.subscription.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    /**all the other functions are by default implemented by JPA
     * save()
     * delete()
     * setters/getters
     * findAll()
     * findById()
     * deleteById();
     */
    List<Subscription> findByUser_Id(UUID userId);
}
