package integra.momentifly.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="task")
@Getter @Setter
public class Task {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch=FetchType.LAZY, optional = true)
    @JoinColumn(name="user_id", nullable = true)
    private User user;

    @Column(length=255, nullable=false)
    private String description;

    @Column(nullable=false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(name = "completed")
    private Boolean completed  = false;
}
