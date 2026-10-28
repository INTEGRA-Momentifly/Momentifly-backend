package integra.momentifly.integration;

import integra.momentifly.model.Appointment;
import integra.momentifly.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    mockMvc.perform(get("/api/appointments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$[0].description").value("Consult de integrare Testcontainers"));
  }
}
