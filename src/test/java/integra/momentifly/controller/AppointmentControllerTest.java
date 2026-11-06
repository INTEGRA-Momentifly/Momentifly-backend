package integra.momentifly.controller;

import integra.momentifly.config.SecurityConfig;
import integra.momentifly.dto.AppointmentRequest;
import integra.momentifly.model.Appointment;
import integra.momentifly.model.User;
import integra.momentifly.repository.UserRepository;
import integra.momentifly.service.AppointmentService;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@Import(SecurityConfig.class)
@WithMockUser(username = "Paul")
public class AppointmentControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;

    @MockitoBean
    private UserRepository userRepository;   // required by SecurityConfig's UserDetailsService bean

    private Appointment appointment;
    private UUID appointmentId;

    private static final String REQUEST_BODY = """
            {
              "description": "Consult stomatologic",
              "startDate": "2026-12-01T10:00:00",
              "endDate": "2026-12-01T11:00:00"
            }
            """;

    @BeforeEach
    void setUpMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        appointmentId = UUID.randomUUID();

        User user = new User();
        user.setUsername("Paul");

        appointment = new Appointment(
                UUID.randomUUID(),
                user,
                "Consult stomatologic",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1)
        );
        appointment.setId(appointmentId);
    }

    @Test
    void testCreateAppointment() throws Exception {
        when(appointmentService.createAppointment(eq("Paul"), any(AppointmentRequest.class))).thenReturn(appointment);

        mockMvc.perform(post("/api/appointments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentId.toString()))
                .andExpect(jsonPath("$.description").value("Consult stomatologic"));
    }

    @Test
    void testGetAllAppointments() throws Exception {
        List<Appointment> appointments = Arrays.asList(appointment);
        when(appointmentService.getAppointmentsForUser("Paul")).thenReturn(appointments);

        mockMvc.perform(get("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].description").value("Consult stomatologic"));
    }

    @Test
    void testGetAppointmentById() throws Exception {
        when(appointmentService.getAppointmentById(appointmentId, "Paul")).thenReturn(appointment);

        mockMvc.perform(get("/api/appointments/{id}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentId.toString()));
    }

    @Test
    void testUpdateAppointment() throws Exception {
        when(appointmentService.updateAppointment(eq(appointmentId), eq("Paul"), any(AppointmentRequest.class))).thenReturn(appointment);

        mockMvc.perform(put("/api/appointments/{id}", appointmentId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Consult stomatologic"));
    }

    @Test
    void testDeleteAppointment() throws Exception {
        doNothing().when(appointmentService).deleteAppointment(appointmentId, "Paul");

        mockMvc.perform(delete("/api/appointments/{id}", appointmentId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}