package integra.momentifly.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "\"user\"") // "user" tells postgres that it's a table name not a keyword
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private int score;

    private Double dailyCalorieGoal;
}
