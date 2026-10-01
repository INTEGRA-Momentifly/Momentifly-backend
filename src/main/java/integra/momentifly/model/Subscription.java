package integra.momentifly.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="subscriptions")
@Getter
@Setter
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch=FetchType.LAZY, optional = true)
    @JoinColumn(name="user_id", nullable = true)
    private User user;

    @Column(nullable = false)
    private Double cost;

    @Column(nullable = false)
    private String name;

    @Column(name="end_date")
    private LocalDateTime endDate;
}
