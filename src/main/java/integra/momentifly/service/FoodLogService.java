package integra.momentifly.service;

import integra.momentifly.dto.AINutritionResponse;
import integra.momentifly.dto.CreateFoodLogRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.FoodLog;
import integra.momentifly.model.User;
import integra.momentifly.repository.FoodLogRepository;
import integra.momentifly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodLogService {

  private final FoodLogRepository foodLogRepository;
  private final AiNutritionService aiNutritionService;
  private final UserRepository userRepository;

  public FoodLog createFoodLog(CreateFoodLogRequest createFoodLogRequest) {
    FoodLog newFoodLog = new FoodLog();

    if (createFoodLogRequest.getUserId() != null) {
      User user = userRepository.findById(createFoodLogRequest.getUserId())
          .orElseThrow(() -> new NotFoundException("User-ul nu a fost găsit cu ID-ul: " + createFoodLogRequest.getUserId()));
      newFoodLog.setUser(user);
    } else {
      throw new InvalidDataException("UserId-ul este obligatoriu pentru a salva un jurnal alimentar.");
    }

    newFoodLog.setDescription(createFoodLogRequest.getDescription());
    newFoodLog.setFoodCategory(createFoodLogRequest.getFoodCategory());
    newFoodLog.setDate(createFoodLogRequest.getDate());

    if (createFoodLogRequest.getProteins() != null &&
        createFoodLogRequest.getCarbs() != null &&
        createFoodLogRequest.getFats() != null &&
        createFoodLogRequest.getCalories() != null) {
      newFoodLog.setCarbs(createFoodLogRequest.getCarbs());
      newFoodLog.setFats(createFoodLogRequest.getFats());
      newFoodLog.setCalories(createFoodLogRequest.getCalories());
      newFoodLog.setProteins(createFoodLogRequest.getProteins());
    } else {
      AINutritionResponse aiResponse = aiNutritionService.estimateNutrients(createFoodLogRequest.getDescription());

      newFoodLog.setProteins(aiResponse.getProteins());
      newFoodLog.setCarbs(aiResponse.getCarbs());
      newFoodLog.setFats(aiResponse.getFats());
      newFoodLog.setCalories(aiResponse.getCalories());
    }

    return foodLogRepository.save(newFoodLog);

  }

  public List<FoodLog> getAllFoodLogsByUserId(UUID userId) {
    return foodLogRepository.findByUser_Id(userId);
  }

//  public Optional<FoodLog> getFoodLogById(UUID id) {
//    return foodLogRepository.findById(id);
//  }

//  public List<FoodLog> getAllFoodLogs() {
//    return foodLogRepository.findAll();
//  }

}
