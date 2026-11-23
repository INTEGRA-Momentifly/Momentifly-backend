package integra.momentifly.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
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

