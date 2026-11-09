package integra.momentifly.controller;

import integra.momentifly.dto.FriendshipResponse;
import integra.momentifly.mapper.FriendshipMapper;
import integra.momentifly.model.Friendship;
import integra.momentifly.model.Status;
import integra.momentifly.service.FriendshipService;
import integra.momentifly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendshipController {

  private final FriendshipService friendshipService;
  private final FriendshipMapper friendshipMapper;
  private final UserService userService;

  @GetMapping()
  public List<FriendshipResponse> getFriendsByStatus(Authentication authentication, @RequestParam(required = false) Status status) {

    UUID userId = userService.getUserByUsername(authentication.getName()).getId();

    List<Friendship> friends = new ArrayList<>();
    if (status != null) {
      friends = friendshipService.getFriendsByStatus(userId, status);
    } else {
      friends = friendshipService.findByUserId(userId);
    }
    return friends.stream()
        .map(friendshipMapper::toDto)
        .toList();
  }

  @PostMapping
  public ResponseEntity<FriendshipResponse> addFriendship(Authentication authentication, @RequestParam UUID friendId) {

    UUID userId = userService.getUserByUsername(authentication.getName()).getId();
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

    if (!friendshipService.findById(id).isPresent()) {
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
