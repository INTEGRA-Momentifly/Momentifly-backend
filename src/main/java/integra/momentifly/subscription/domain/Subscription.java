package integra.momentifly.subscription.domain;

import integra.momentifly.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
// * If we delete a user, should the database automatically remove the users' subscriptions?
 *
 */
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
     *this is part of the JPA course
     * */
    @ManyToOne(fetch=FetchType.LAZY)
    /*this is a way to make the id of the user a foreign key of the table based on the diagram with the dependence relationship*/
    @JoinColumn(name="user_id", nullable = false)

    private User user;

    @Column(nullable = false)
    private Double cost;

    @Column(nullable = false)
    private String name;

    @Column(name="end_date")
    private LocalDateTime endDate;
}
