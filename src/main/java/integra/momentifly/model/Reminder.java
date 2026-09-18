package integra.momentifly.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminder")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "description")
    private String description;

    @Column(name = "reminderDate")
    private LocalDateTime reminderDate;

    @Column(name = "done")
    private Boolean done;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence")
    private RecurrenceEnum recurrence;
}
