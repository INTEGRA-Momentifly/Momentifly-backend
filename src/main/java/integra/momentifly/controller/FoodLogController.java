package integra.momentifly.controller;

import integra.momentifly.dto.CreateFoodLogRequest;
import integra.momentifly.model.FoodLog;
import integra.momentifly.service.FoodLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("api/food_log")
@RequiredArgsConstructor
public class FoodLogController {

  private final FoodLogService foodLogService;

  @PostMapping
  public ResponseEntity<FoodLog> createFoodLog(@Valid @RequestBody CreateFoodLogRequest createFoodLogRequest){
      return ResponseEntity.ok(foodLogService.createFoodLog(createFoodLogRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<FoodLog> getFoodLogById(@PathVariable UUID id) {
    return foodLogService.getFoodLogById(id).
        map(ResponseEntity::ok).
        orElse(ResponseEntity.notFound().build());
  }
}
