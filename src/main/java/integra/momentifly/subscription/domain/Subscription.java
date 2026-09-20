package integra.momentifly.subscription.domain;

import integra.momentifly.model.User;
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

    /**
     * many subscriptions to a single user, so you have to think like if multiple objects of this class can be assigned to an object
     * of a different type, not the other way around
     * the FetchType is just a way to retrieve data from the object
     * LAZY-it's like asking: "Hey do you know anything about X", and they give you answers (data is loaded only if you request for it
     * e.g. subscription.getUser()).
     * EAGER-2nd way of retrieving, someone tells you something without asking for it. (data is loaded immediately upon object creation).
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
//this is a way to make the id of the user a foreign key of the table based on the diagram with the dependance relationship
    private User user;
//both are objects not primitive types so the instances can have null values

    private double cost;

    private String name;

    private LocalDateTime endDate;
}
