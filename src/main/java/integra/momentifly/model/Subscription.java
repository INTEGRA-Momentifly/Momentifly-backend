package integra.momentifly.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private Double cost;

    @Column(nullable = false)
    private String name;

    @Column(name="end_date")
    private LocalDateTime endDate;
}
