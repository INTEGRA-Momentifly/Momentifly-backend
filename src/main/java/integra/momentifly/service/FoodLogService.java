package integra.momentifly.service;

import integra.momentifly.dto.CreateFoodLogRequest;
import integra.momentifly.model.FoodLog;
import integra.momentifly.model.User;
import integra.momentifly.repository.FoodLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodLogService {

  private final FoodLogRepository foodLogRepository;

  public FoodLog createFoodLog(CreateFoodLogRequest createFoodLogRequest) {
    FoodLog newFoodLog = new FoodLog();

    // Atribuim un dummy user cu ID-ul primit pentru a satisface cheia străină NOT NULL
    if (createFoodLogRequest.getUserId() != null) {
      User user = new User();
      user.setId(createFoodLogRequest.getUserId());
      newFoodLog.setUser(user);
    }

    newFoodLog.setDescription(createFoodLogRequest.getDescription());
    newFoodLog.setFoodCategory(createFoodLogRequest.getFoodCategory());
    newFoodLog.setProteins(createFoodLogRequest.getProteins());
    newFoodLog.setCarbs(createFoodLogRequest.getCarbs());
    newFoodLog.setFats(createFoodLogRequest.getFats());
    newFoodLog.setCalories(createFoodLogRequest.getCalories());
    newFoodLog.setDate(createFoodLogRequest.getDate());

    return foodLogRepository.save(newFoodLog);
  }

  public Optional<FoodLog> getFoodLogById(UUID id) {
    return foodLogRepository.findById(id);
  }
}
