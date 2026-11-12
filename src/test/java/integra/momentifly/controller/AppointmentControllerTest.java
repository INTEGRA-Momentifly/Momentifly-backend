package integra.momentifly.controller;

import integra.momentifly.dto.AppointmentRequest;
import integra.momentifly.model.Appointment;
import integra.momentifly.service.AppointmentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

  @Mock
  private AppointmentService appointmentService;

  @InjectMocks
  private AppointmentController appointmentController;

  private Appointment appointment;
  private UUID appointmentId;

  @BeforeEach
  void setUp() {
    appointmentId = UUID.randomUUID();
    appointment = new Appointment(
        UUID.randomUUID(),
        UUID.randomUUID(),
        "Consult stomatologic",
        LocalDateTime.now().plusDays(1),
        LocalDateTime.now().plusDays(1).plusHours(1)
    );
    appointment.setId(appointmentId);
  }

  @Test
  void testCreateAppointment() {
    AppointmentRequest request = new AppointmentRequest();
    request.setUserId(appointment.getUserId());
    request.setDescription(appointment.getDescription());
    request.setStartDate(appointment.getStartDate());
    request.setEndDate(appointment.getEndDate());

    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);

    ResponseEntity<Appointment> response = appointmentController.createAppointment(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(appointment, response.getBody());
  }

  @Test
  void testGetAllAppointments_WithoutParams() {
    List<Appointment> appointments = List.of(appointment);

    when(appointmentService.getOverlappingAppointments(null, null)).thenReturn(appointments);

    ResponseEntity<List<Appointment>> response = appointmentController.getAllAppointments(null, null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    assertEquals(appointment, response.getBody().get(0));
  }

  @Test
  void testGetAllAppointments_WithDateRange() {
    LocalDateTime start = LocalDateTime.now();
    LocalDateTime end = LocalDateTime.now().plusDays(2);
    List<Appointment> appointments = List.of(appointment);

    when(appointmentService.getOverlappingAppointments(start, end)).thenReturn(appointments);

    ResponseEntity<List<Appointment>> response = appointmentController.getAllAppointments(start, end);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void testGetAppointmentById_WhenExists() {
    when(appointmentService.getAppointmentById(appointmentId)).thenReturn(Optional.of(appointment));

    ResponseEntity<Appointment> response = appointmentController.getAppointmentById(appointmentId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(appointment, response.getBody());
  }

  @Test
  void testGetAppointmentById_WhenNotFound() {
    when(appointmentService.getAppointmentById(appointmentId)).thenReturn(Optional.empty());

    ResponseEntity<Appointment> response = appointmentController.getAppointmentById(appointmentId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testUpdateAppointment() {
    AppointmentRequest request = new AppointmentRequest();
    request.setUserId(appointment.getUserId());
    request.setDescription(appointment.getDescription());
    request.setStartDate(appointment.getStartDate());
    request.setEndDate(appointment.getEndDate());

    when(appointmentService.updateAppointment(eq(appointmentId), any(Appointment.class)))
        .thenReturn(appointment);

    ResponseEntity<Appointment> response = appointmentController.updateAppointment(appointmentId, request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(appointment, response.getBody());
  }

  @Test
  void testDeleteAppointment() {
    doNothing().when(appointmentService).deleteAppointment(appointmentId);

    ResponseEntity<Void> response = appointmentController.deleteAppointment(appointmentId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }
}
