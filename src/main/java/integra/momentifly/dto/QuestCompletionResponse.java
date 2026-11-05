package integra.momentifly.dto;

import integra.momentifly.model.QuestEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestCompletionResponse {
  private UUID id;
  private double points;
  private UUID userId;
  private UUID questId;
  private LocalDateTime completionDate;
  private QuestEnum completionStatus;
}
