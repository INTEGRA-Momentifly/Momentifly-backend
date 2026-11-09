package integra.momentifly.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name="objective",
uniqueConstraints = {
        @UniqueConstraint(
                name = "uq_objective_user_type",
                columnNames = {"user_id", "type"}
        )
})
@Getter
@Setter
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MacroType type;

  @Column(nullable=false)
  private Integer value;
}
