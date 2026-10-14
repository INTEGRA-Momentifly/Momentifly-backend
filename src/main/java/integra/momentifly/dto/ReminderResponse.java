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
public class ReminderResponse {

    private UUID id;

    private UUID userId;

    private String description;

    private LocalDateTime reminderDate;

    private Boolean done;

    private RecurrenceEnum recurrence;
}
