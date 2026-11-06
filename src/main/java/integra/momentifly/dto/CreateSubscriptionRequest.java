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
    private double cost;

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime endDate;
}
