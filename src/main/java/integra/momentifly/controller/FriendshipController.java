package integra.momentifly.controller;

import integra.momentifly.dto.FriendshipResponse;
import integra.momentifly.mapper.FriendshipMapper;
import integra.momentifly.model.Friendship;
import integra.momentifly.model.Status;
import integra.momentifly.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final FriendshipMapper friendshipMapper;

    @GetMapping()
    public List<FriendshipResponse> getFriendsByStatus(@RequestParam UUID userId, @RequestParam Status status) {

        List<Friendship> friends = friendshipService.getFriendsByStatus(userId, status);

        return friends.stream()
                .map(friendshipMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<FriendshipResponse> addFriendship(@RequestParam UUID userId, @RequestParam UUID friendId) {

        if (userId.equals(friendId)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Friendship> newFriendship = friendshipService.addFriendship(userId, friendId);

        if (newFriendship.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(friendshipMapper.toDto(newFriendship.get()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FriendshipResponse> updateStatus(@PathVariable UUID id, @RequestParam Status status) {

        if (!friendshipService.findById(id).isPresent()){
            return ResponseEntity.notFound().build();
        }

        Friendship friendship = friendshipService.findById(id).get();

        friendship.setStatus(status);
        friendshipService.save(friendship);

        FriendshipResponse response = friendshipMapper.toDto(friendship);

        return ResponseEntity.ok(response);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        if (!friendshipService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        friendshipService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
