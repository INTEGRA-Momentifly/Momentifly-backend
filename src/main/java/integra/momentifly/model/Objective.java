package integra.momentifly.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name="objective")
public class Objective {

  @Getter
  @Id
  @GeneratedValue(strategy= GenerationType.UUID)
  private UUID id;

  // join column, many to one relationship here but only once the user model is finished
  @Getter @Setter
  @Column(columnDefinition = "uuid", nullable = false)
  private UUID userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Getter @Setter
  private MacroType type;

  @Column(nullable=false)
  @Getter @Setter
  private Integer value;
}
