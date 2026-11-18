package integra.momentifly.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AINutritionResponse {
  private Integer proteins;

  private Integer carbs;

  private Integer fats;

  private Integer calories;
}
