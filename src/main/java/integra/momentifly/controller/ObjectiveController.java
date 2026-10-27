package integra.momentifly.controller;

import integra.momentifly.dto.WriteObjectiveRequest;
import integra.momentifly.service.ObjectiveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/objectives")
@RequiredArgsConstructor
public class ObjectiveController {
    private final ObjectiveService objectiveService;

    // CHANGE TO AUTHENTICATION OBJECT AFTER USER MODEL + CRUDS EXIST ON MAIN
    @GetMapping("/{userId}")
    public ResponseEntity<?> getObjectivesForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(objectiveService.getAllObjectivesForUser(userId));
    }

    @PutMapping
    public ResponseEntity<?> updateObjective(@Valid @RequestBody WriteObjectiveRequest request) {
        return ResponseEntity.ok(objectiveService.updateObjective(request));
    }

    @PostMapping
    public ResponseEntity<?> createObjective(@Valid @RequestBody WriteObjectiveRequest request) {
         return ResponseEntity.ok(objectiveService.addObjective(request));
    }
}
