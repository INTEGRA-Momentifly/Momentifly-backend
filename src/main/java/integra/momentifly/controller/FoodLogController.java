package integra.momentifly.controller;

import integra.momentifly.dto.CreateFoodLogRequest;
import integra.momentifly.model.FoodLog;
import integra.momentifly.service.FoodLogService;
import integra.momentifly.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/food_log")
@RequiredArgsConstructor
public class FoodLogController {

  private final FoodLogService foodLogService;
  private final UserService userService;

  @PostMapping
  public ResponseEntity<FoodLog> createFoodLog(@Valid @RequestBody CreateFoodLogRequest createFoodLogRequest,
                                               Authentication authentication) {
    UUID userId = userService.getUserByUsername(authentication.getName()).getId();

    createFoodLogRequest.setUserId(userId);

    return ResponseEntity.ok(foodLogService.createFoodLog(createFoodLogRequest));
  }


  @GetMapping
  public ResponseEntity<List<FoodLog>> getFoodLogsByUserId(Authentication authentication) {
    UUID userId = userService.getUserByUsername(authentication.getName()).getId();

    return ResponseEntity.ok(foodLogService.getAllFoodLogsByUserId(userId));
  }
}

//  @GetMapping("/{id}")
//  public ResponseEntity<FoodLog> getFoodLogById(@PathVariable UUID id) {
//    return foodLogService.getFoodLogById(id).
//        map(ResponseEntity::ok).
//        orElse(ResponseEntity.notFound().build());
//  }

//  @GetMapping
//  public ResponseEntity<List<FoodLog>> getFoodLogs(@RequestParam(required = false) UUID userId){
//    if (userId != null) {
//      return ResponseEntity.ok(foodLogService.getAllFoodLogsByUserId(userId));
//    }
//
//    return ResponseEntity.ok(foodLogService.getAllFoodLogs());
//  }
