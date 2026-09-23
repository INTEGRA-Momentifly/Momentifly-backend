package integra.momentifly.dto;

import integra.momentifly.model.RecurrenceEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReminderDtoIn {

    @NotNull
    private UUID userId;

    @NotBlank
    private String description;

    private LocalDateTime reminderDate;

    @NotNull
    private Boolean done;

    @NotNull
    private RecurrenceEnum recurrence;
}
