package integra.momentifly.service;

import integra.momentifly.model.Friendship;
import integra.momentifly.model.Reminder;
import integra.momentifly.model.Status;
import integra.momentifly.model.User;
import integra.momentifly.repository.FriendshipRepository;
import integra.momentifly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public List<Friendship> getFriendsByStatus(UUID userId, Status status) {
        return friendshipRepository.findByUserIdAndStatusOrFriendIdAndStatus(
                userId, status,
                userId, status
        );
    }

    public Optional<Friendship> findById(UUID id) {
        return friendshipRepository.findById(id);
    }

    public Friendship save(Friendship friendship) {
        return friendshipRepository.save(friendship);
    }

    public Optional<Friendship> addFriendship(UUID userId, UUID friendId) {

        boolean exists = friendshipRepository.existsByUserIdAndFriendIdOrUserIdAndFriendId(
                userId, friendId,
                friendId, userId
        );
        if (exists) {
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<User> friendOpt = userRepository.findById(friendId);

        if (userOpt.isEmpty() || friendOpt.isEmpty()) {
            return Optional.empty();
        }

        Friendship friendship = new Friendship();
        friendship.setUser(userOpt.get());
        friendship.setFriend(friendOpt.get());
        friendship.setFriendshipDate(LocalDateTime.now());

        return Optional.of(friendshipRepository.save(friendship));
    }

    public void deleteById(UUID friendshipId){
        friendshipRepository.deleteById(friendshipId);
    }
}
