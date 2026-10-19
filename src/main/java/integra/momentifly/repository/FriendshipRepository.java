package integra.momentifly.repository;

import integra.momentifly.model.Friendship;
import integra.momentifly.model.Status;
import integra.momentifly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {
    List<Friendship> findByUserId( UUID userId);
    List<Friendship> findByFriendId( UUID friendId);
    List<Friendship> findByUserIdAndStatusOrFriendIdAndStatus(
            UUID userId1, Status status1,
            UUID userId2, Status status2
    );
    boolean existsByUserIdAndFriendIdOrUserIdAndFriendId(
            UUID userId1, UUID friendId1,
            UUID userId2, UUID friendId2
    );
}
