package integra.momentifly.dto;

import integra.momentifly.model.RecurrenceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReminderDtoOut {

    private UUID id;

    private String description;

    private LocalDateTime reminderDate;

    private Boolean done;

    private RecurrenceEnum recurrence;
}
