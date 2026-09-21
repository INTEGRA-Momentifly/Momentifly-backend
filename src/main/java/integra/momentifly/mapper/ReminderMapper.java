package integra.momentifly.mapper;

import integra.momentifly.dto.ReminderDtoIn;
import integra.momentifly.dto.ReminderDtoOut;
import integra.momentifly.model.Reminder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReminderMapper {
    ReminderDtoOut toDto(Reminder reminder);

    @Mapping(target = "id", ignore = true)
    Reminder fromDto(ReminderDtoIn reminderDtoIn);
}
