package integra.momentifly.integration;

import integra.momentifly.model.Appointment;
import integra.momentifly.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class AppointmentIntegrationTest {

    // 1. Definim containerul de PostgreSQL
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("momentifly_integration_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    // 2. Suprascriem proprietățile de conexiune din application.properties pentru a folosi containerul
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // 3. Seeding-ul bazei de date înainte de fiecare test
    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll(); // Curățăm datele vechi

        Appointment appointment = new Appointment();
        appointment.setUserId(UUID.randomUUID());
        appointment.setDescription("Consult de integrare Testcontainers");
        appointment.setStartDate(LocalDateTime.now().plusDays(1));
        appointment.setEndDate(LocalDateTime.now().plusDays(1).plusHours(1));

        appointmentRepository.save(appointment);
    }

    // 4. Testul efectiv care lovește baza de date reală
    @Test
    void shouldReturnAppointmentsFromRealDatabase() throws Exception {
        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].description").value("Consult de integrare Testcontainers"));
    }
}