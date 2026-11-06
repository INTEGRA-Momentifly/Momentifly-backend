package integra.momentifly.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import integra.momentifly.model.Appointment;
import integra.momentifly.repository.AppointmentRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class AppointmentIntegrationTest extends BaseIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AppointmentRepository appointmentRepository;

  @BeforeEach
  void setUp() {
    appointmentRepository.deleteAll();

    Appointment appointment = new Appointment();
    appointment.setUserId(UUID.randomUUID());
    appointment.setDescription("Consult de integrare Testcontainers");
    appointment.setStartDate(LocalDateTime.now().plusDays(1));
    appointment.setEndDate(LocalDateTime.now().plusDays(1).plusHours(1));

    appointmentRepository.save(appointment);
  }

  @Test
  void shouldReturnAppointmentsFromRealDatabase() throws Exception {
    mockMvc.perform(get("/api/appointments").with(user("test").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$[0].description").value("Consult de integrare Testcontainers"));
  }

  @Test
  void shouldReturn401WhenNotAuthenticated() throws Exception {
    mockMvc.perform(get("/api/appointments"))
        .andExpect(status().isUnauthorized());
  }
}
