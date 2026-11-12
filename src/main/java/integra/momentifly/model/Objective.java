package integra.momentifly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
