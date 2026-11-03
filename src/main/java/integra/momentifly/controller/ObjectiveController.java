package integra.momentifly.controller;

import integra.momentifly.dto.ObjectiveResponse;
import integra.momentifly.dto.WriteObjectiveRequest;
import integra.momentifly.service.ObjectiveService;
import integra.momentifly.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/objectives")
@RequiredArgsConstructor
public class ObjectiveController {
    private final ObjectiveService objectiveService;

    @GetMapping
    public ResponseEntity<?> getObjectives(Authentication authentication) {
        return ResponseEntity.ok(objectiveService.getObjectives(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<?> createObjective(Authentication authentication,
                                                              @Valid @RequestBody WriteObjectiveRequest request) {
        return ResponseEntity.ok(objectiveService.addObjective(authentication.getName(), request));
    }

    @PutMapping
    public ResponseEntity<?> updateObjective(Authentication authentication,
                                                              @Valid @RequestBody WriteObjectiveRequest request) {
        return ResponseEntity.ok(objectiveService.updateObjective(authentication.getName(), request));
    }
}
