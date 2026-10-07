package integra.momentifly.controller;

import integra.momentifly.dto.ReminderRequest;
import integra.momentifly.dto.ReminderResponse;
import integra.momentifly.mapper.ReminderMapper;
import integra.momentifly.model.RecurrenceEnum;
import integra.momentifly.model.Reminder;
import integra.momentifly.service.ReminderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReminderController.class)
class ReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReminderService reminderService;

    @MockitoBean
    private ReminderMapper mapper;

    private final UUID reminderId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();

    private Reminder createReminder() {
        Reminder reminder = new Reminder();
        reminder.setId(reminderId);
        reminder.setUserId(userId);
        reminder.setDescription("Test reminder");
        reminder.setReminderDate(LocalDateTime.of(2026, 9, 23, 18, 0));
        reminder.setDone(false);
        reminder.setRecurrence(RecurrenceEnum.WEEKLY);
        return reminder;
    }

    private ReminderResponse createReminderDtoOut() {
        return new ReminderResponse(
                reminderId,
                userId,
                "Test reminder",
                LocalDateTime.of(2026, 9, 23, 18, 0),
                false,
                RecurrenceEnum.WEEKLY
        );
    }

    @Test
    void shouldFindAllReminders() throws Exception {
        Reminder reminder = createReminder();
        ReminderResponse dto = createReminderDtoOut();

        when(reminderService.findAll())
                .thenReturn(List.of(reminder));

        when(mapper.toDto(reminder))
                .thenReturn(dto);

        mockMvc.perform(get("/reminder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reminderId.toString()))
                .andExpect(jsonPath("$[0].userId").value(userId.toString()))
                .andExpect(jsonPath("$[0].description").value("Test reminder"))
                .andExpect(jsonPath("$[0].done").value(false))
                .andExpect(jsonPath("$[0].recurrence").value("WEEKLY"));

        verify(reminderService).findAll();
        verify(mapper).toDto(reminder);
    }

    @Test
    void shouldFindReminderById() throws Exception {
        Reminder reminder = createReminder();
        ReminderResponse dto = createReminderDtoOut();

        when(reminderService.findById(reminderId))
                .thenReturn(Optional.of(reminder));

        when(mapper.toDto(reminder))
                .thenReturn(dto);

        mockMvc.perform(get("/reminder/{id}", reminderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reminderId.toString()))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.description").value("Test reminder"))
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.recurrence").value("WEEKLY"));

        verify(reminderService).findById(reminderId);
        verify(mapper).toDto(reminder);
    }

    @Test
    void shouldFindRemindersByUserId() throws Exception {
        Reminder reminder = createReminder();
        ReminderResponse dto = createReminderDtoOut();

        when(reminderService.findByUserId(userId))
                .thenReturn(List.of(reminder));

        when(mapper.toDto(reminder))
                .thenReturn(dto);

        mockMvc.perform(get("/reminder/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reminderId.toString()))
                .andExpect(jsonPath("$[0].userId").value(userId.toString()))
                .andExpect(jsonPath("$[0].description").value("Test reminder"))
                .andExpect(jsonPath("$[0].done").value(false))
                .andExpect(jsonPath("$[0].recurrence").value("WEEKLY"));

        verify(reminderService).findByUserId(userId);
        verify(mapper).toDto(reminder);
    }

    @Test
    void shouldCreateReminder() throws Exception {
        Reminder reminder = createReminder();
        ReminderResponse output = createReminderDtoOut();

        when(mapper.fromDto(any(ReminderRequest.class)))
                .thenReturn(reminder);

        when(reminderService.save(reminder))
                .thenReturn(reminder);

        when(mapper.toDto(reminder))
                .thenReturn(output);

        mockMvc.perform(post("/reminder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "%s",
                                  "description": "Test reminder",
                                  "reminderDate": "2026-09-23T18:00:00",
                                  "done": false,
                                  "recurrence": "WEEKLY"
                                }
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reminderId.toString()))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.description").value("Test reminder"))
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.recurrence").value("WEEKLY"));

        verify(mapper).fromDto(any(ReminderRequest.class));
        verify(reminderService).save(reminder);
        verify(mapper).toDto(reminder);
    }

    @Test
    void shouldUpdateReminder() throws Exception {
        Reminder existingReminder = createReminder();
        Reminder updatedReminder = createReminder();
        updatedReminder.setDescription("Updated reminder");

        ReminderResponse output = new ReminderResponse(
                reminderId,
                userId,
                "Updated reminder",
                LocalDateTime.of(2026, 9, 23, 18, 0),
                false,
                RecurrenceEnum.WEEKLY
        );

        when(reminderService.findById(reminderId))
                .thenReturn(Optional.of(existingReminder));

        when(mapper.fromDto(any(ReminderRequest.class)))
                .thenReturn(updatedReminder);

        when(reminderService.save(any(Reminder.class)))
                .thenReturn(updatedReminder);

        when(mapper.toDto(updatedReminder))
                .thenReturn(output);

        mockMvc.perform(put("/reminder/{id}", reminderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "%s",
                                  "description": "Updated reminder",
                                  "reminderDate": "2026-09-23T18:00:00",
                                  "done": false,
                                  "recurrence": "WEEKLY"
                                }
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reminderId.toString()))
                .andExpect(jsonPath("$.description").value("Updated reminder"))
                .andExpect(jsonPath("$.recurrence").value("WEEKLY"));

        verify(reminderService).findById(reminderId);
        verify(mapper).fromDto(any(ReminderRequest.class));
        verify(reminderService).save(any(Reminder.class));
        verify(mapper).toDto(updatedReminder);
    }

    @Test
    void shouldDeleteReminder() throws Exception {
        when(reminderService.findById(reminderId))
                .thenReturn(Optional.of(createReminder()));

        mockMvc.perform(delete("/reminder/{id}", reminderId))
                .andExpect(status().isNoContent());

        verify(reminderService).findById(reminderId);
        verify(reminderService).deleteById(reminderId);
    }
}