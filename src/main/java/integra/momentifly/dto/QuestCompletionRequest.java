package integra.momentifly.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestCompletionRequest {
  @NotNull
  private double points;
  @NotNull
  private UUID userId;
  @NotNull
  private UUID questId;
}
