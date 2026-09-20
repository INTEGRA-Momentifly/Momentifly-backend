package integra.momentifly.subscription.repository;

import integra.momentifly.subscription.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    /**all the other functions are by default implemented by JPA
     * save()
     * delete()
     * setters/getters
     * findAll()
     * findById()
     * deleteById();
     */
    List<Subscription> findByUser_Id(Long userId);
}
