package integra.momentifly.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "questcompletion")
public class QuestCompletion {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "quest_id", nullable = false)
  private UUID questId;

  @Column(name = "completion_date")
  private LocalDateTime completionDate;

  @Column(name = "points")
  private double points;

  @Enumerated(EnumType.STRING)
  @Column(name = "completion_status")
  private QuestEnum completionStatus;
}

