package integra.momentifly.mapper;

import integra.momentifly.dto.ReminderRequest;
import integra.momentifly.dto.ReminderResponse;
import integra.momentifly.model.Reminder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReminderMapper {
    ReminderResponse toDto(Reminder reminder);

    @Mapping(target = "id", ignore = true)
    Reminder fromDto(ReminderRequest reminderRequest);
}
