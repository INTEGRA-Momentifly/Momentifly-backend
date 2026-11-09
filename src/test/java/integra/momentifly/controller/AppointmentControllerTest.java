package integra.momentifly.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import integra.momentifly.model.Appointment;
import integra.momentifly.service.AppointmentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AppointmentService appointmentService;

  @Autowired
  private ObjectMapper objectMapper;

  private Appointment appointment;
  private UUID appointmentId;
  private UUID userId;

  @BeforeEach
  void setUp() {
    appointmentId = UUID.randomUUID();
    userId = UUID.randomUUID();

    appointment = new Appointment(
        appointmentId,
        userId,
        "Consult stomatologic",
        LocalDateTime.now().plusDays(1),
        LocalDateTime.now().plusDays(1).plusHours(1)
    );
  }

  @Test
  void testCreateAppointment() throws Exception {
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    mockMvc.perform(post("/api/appointments")
            .with(user("test").roles("USER"))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(appointmentId.toString()))
        .andExpect(jsonPath("$.userId").value(userId.toString()))
        .andExpect(jsonPath("$.description").value("Consult stomatologic"));
  }

  @Test
  void testGetAllAppointments() throws Exception {
    when(appointmentService.getAllAppointments()).thenReturn(List.of(appointment));

    mockMvc.perform(get("/api/appointments")
            .with(user("test").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$[0].description").value("Consult stomatologic"));
  }

  @Test
  void testGetAppointmentById() throws Exception {
    when(appointmentService.getAppointmentById(appointmentId)).thenReturn(Optional.of(appointment));

    mockMvc.perform(get("/api/appointments/{id}", appointmentId)
            .with(user("test").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(appointmentId.toString()));
  }

  @Test
  void testUpdateAppointment() throws Exception {
    when(appointmentService.updateAppointment(eq(appointmentId), any(Appointment.class)))
        .thenReturn(appointment);

    mockMvc.perform(put("/api/appointments/{id}", appointmentId)
            .with(user("test").roles("USER"))
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.description").value("Consult stomatologic"));
  }

  @Test
  void testDeleteAppointment() throws Exception {
    doNothing().when(appointmentService).deleteAppointment(appointmentId);

    mockMvc.perform(delete("/api/appointments/{id}", appointmentId)
            .with(user("test").roles("USER"))
            .with(csrf()))
        .andExpect(status().isNoContent());
  }
}
