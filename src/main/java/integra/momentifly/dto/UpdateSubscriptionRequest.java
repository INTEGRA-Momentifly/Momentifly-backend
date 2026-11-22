package integra.momentifly.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class UpdateSubscriptionRequest {
    @NotNull
    private double cost;

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime endDate;
}
