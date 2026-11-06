package integra.momentifly.controller;

import integra.momentifly.config.SecurityConfig;
import integra.momentifly.dto.ReminderRequest;
import integra.momentifly.dto.ReminderResponse;
import integra.momentifly.mapper.ReminderMapper;
import integra.momentifly.model.RecurrenceEnum;
import integra.momentifly.model.Reminder;
import integra.momentifly.model.User;
import integra.momentifly.repository.UserRepository;
import integra.momentifly.service.ReminderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReminderController.class)
@Import(SecurityConfig.class)
@WithMockUser(username = "Paul")
class ReminderControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private ReminderService reminderService;

    @MockitoBean
    private ReminderMapper mapper;

    @MockitoBean
    private UserRepository userRepository;   // required by SecurityConfig's UserDetailsService bean

    private final UUID reminderId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUpMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private Reminder createReminder() {
        User user = new User();
        user.setId(userId);
        user.setUsername("Paul");

        Reminder reminder = new Reminder();
        reminder.setId(reminderId);
        reminder.setUser(user);
        reminder.setDescription("Test reminder");
        reminder.setReminderDate(LocalDateTime.of(2026, 9, 23, 18, 0));
        reminder.setDone(false);
        reminder.setRecurrence(RecurrenceEnum.WEEKLY);
        return reminder;
    }

    private ReminderResponse createReminderDtoOut() {
        return new ReminderResponse(
                reminderId,
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

        when(reminderService.findByUsername("Paul"))
                .thenReturn(List.of(reminder));

        when(mapper.toDto(reminder))
                .thenReturn(dto);

        mockMvc.perform(get("/api/reminder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reminderId.toString()))
                .andExpect(jsonPath("$[0].description").value("Test reminder"))
                .andExpect(jsonPath("$[0].done").value(false))
                .andExpect(jsonPath("$[0].recurrence").value("WEEKLY"));

        verify(reminderService).findByUsername("Paul");
        verify(mapper).toDto(reminder);
    }

    @Test
    void shouldFindReminderById() throws Exception {
        Reminder reminder = createReminder();
        ReminderResponse dto = createReminderDtoOut();

        when(reminderService.findById(reminderId, "Paul"))
                .thenReturn(reminder);

        when(mapper.toDto(reminder))
                .thenReturn(dto);

        mockMvc.perform(get("/api/reminder/{id}", reminderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reminderId.toString()))
                .andExpect(jsonPath("$.description").value("Test reminder"))
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.recurrence").value("WEEKLY"));

        verify(reminderService).findById(reminderId, "Paul");
        verify(mapper).toDto(reminder);
    }

    @Test
    @WithMockUser(username = "Andrei")
    void shouldOnlyFindRemindersOfTheLoggedInUser() throws Exception {
        Reminder reminder = createReminder();
        ReminderResponse dto = createReminderDtoOut();

        when(reminderService.findByUsername("Andrei"))
                .thenReturn(List.of(reminder));

        when(mapper.toDto(reminder))
                .thenReturn(dto);

        mockMvc.perform(get("/api/reminder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reminderId.toString()))
                .andExpect(jsonPath("$[0].description").value("Test reminder"))
                .andExpect(jsonPath("$[0].done").value(false))
                .andExpect(jsonPath("$[0].recurrence").value("WEEKLY"));
    }
}