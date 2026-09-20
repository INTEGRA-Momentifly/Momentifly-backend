package integra.momentifly.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="task")
public class Task {

    @Getter
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    // setter for now but remove later
    // join column, many to one relationship here but only once the user model is finished
    @Getter @Setter
    @Column(columnDefinition = "uuid", nullable = false)
    private UUID userId;

    @Getter @Setter
    @Column(length=255, nullable=false)
    private String description;

    @Getter @Setter
    @Column(nullable=false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter @Setter
    private DifficultyEnum difficulty;

    @Getter @Setter
    private Boolean completed  = false;
}
