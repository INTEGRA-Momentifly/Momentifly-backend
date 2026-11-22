package integra.momentifly.integration;

import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.Appointment;
import integra.momentifly.model.User;
import integra.momentifly.repository.AppointmentRepository;
import integra.momentifly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AppointmentIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // works assuming the dataseeder does exist, and it creates the user with this username
        User Paul = userRepository.findByUsername("Paul").orElseThrow(
                () -> new NotFoundException("Paul not found"));
        appointmentRepository.deleteAll();

        Appointment appointment = new Appointment();
        appointment.setUser(Paul);
        appointment.setDescription("Consult de integrare Testcontainers");
        appointment.setStartDate(LocalDateTime.now().plusDays(1));
        appointment.setEndDate(LocalDateTime.now().plusDays(1).plusHours(1));

        appointmentRepository.save(appointment);
    }

    @Test
    void shouldReturnAppointmentsFromRealDatabase() throws Exception {
        mockMvc.perform(get("/api/appointments")
                        .with(httpBasic("Paul", "test123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].description")
                        .value("Consult de integrare Testcontainers"));
    }

    @Test
    void shouldReturn401WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isUnauthorized());
    }
}