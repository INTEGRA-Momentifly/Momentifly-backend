package integra.momentifly.dto;

import integra.momentifly.model.FoodCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class CreateFoodLogRequest {

  //  Lăsat fără @NotNull temporar, sau menținut dacă frontend-ul trimite deja un ID valid
  private UUID userId;

  @NotNull
  private String description;

  @NotNull
  private FoodCategory foodCategory;

  @Min(0)
  private int proteins;

  @Min(0)
  private int carbs;

  @Min(0)
  private int fats;

  @Min(0)
  private int calories;

  @NotNull
  private LocalDate date;

}
