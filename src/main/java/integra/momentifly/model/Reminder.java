package integra.momentifly.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reminder")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "description")
    private String description;

    @Column(name = "reminder_date")
    private LocalDateTime reminderDate;

    @Column(name = "done")
    private Boolean done;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence")
    private RecurrenceEnum recurrence;
}
