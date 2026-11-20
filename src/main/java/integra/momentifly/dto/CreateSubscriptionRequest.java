package integra.momentifly.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class CreateSubscriptionRequest {
  @NotNull
  private UUID userId;

  @NotNull
  private double cost;

  @NotNull
  private String name;

  @NotNull
  private boolean autoRenewal;

  @NotNull
  private LocalDateTime endDate;
}
