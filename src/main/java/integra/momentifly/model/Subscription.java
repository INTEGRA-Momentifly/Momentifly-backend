package integra.momentifly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="subscription")
@Getter
@Setter
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
