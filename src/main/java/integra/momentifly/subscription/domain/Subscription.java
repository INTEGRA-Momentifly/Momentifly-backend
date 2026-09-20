package integra.momentifly.subscription.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="subscriptions")
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//this property is not in the diagram but was added for database reasons (it's the subscription ID)

    private Long userId;
//both are objects not primitive types so the instances can have null values

    private double cost;

    private String name;

    private LocalDateTime endDate;
}
