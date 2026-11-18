package integra.momentifly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "food_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodLog {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private String description;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "food_category", nullable = false)
  private FoodCategory foodCategory;

  @Column(nullable = false)
  private int proteins;

  @Column(nullable = false)
  private int carbs;

  @Column(nullable = false)
  private int fats;

  @Column(nullable = false)
  private int calories;

  @Column(nullable = false)
  private LocalDate date;
}
