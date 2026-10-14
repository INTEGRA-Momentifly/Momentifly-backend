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
//  Lăsat fără @NotNull temporar, sau menținut dacă frontend-ul trimite deja un ID valid
    private UUID userId;

    @NotNull
    private double cost;

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime endDate;
}
