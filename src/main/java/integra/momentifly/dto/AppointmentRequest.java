package integra.momentifly.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentRequest {
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}