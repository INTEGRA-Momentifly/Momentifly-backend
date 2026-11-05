package integra.momentifly.controller;

import integra.momentifly.dto.AppointmentRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.model.Appointment;
import integra.momentifly.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

  private final AppointmentService service;

  // Create an Appointment
  @PostMapping
  public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest request) {
    // Mapăm datele din DTO (cerere) în Entitate
    Appointment appointment = new Appointment();
    appointment.setUserId(request.getUserId());
    appointment.setDescription(request.getDescription());
    appointment.setStartDate(request.getStartDate());
    appointment.setEndDate(request.getEndDate());
    return ResponseEntity.ok(service.createAppointment(appointment));
  }

  // List all Appointments
  @GetMapping
  public ResponseEntity<List<Appointment>> getAllAppointments(
      @RequestParam(required = false) LocalDateTime startDate,
      @RequestParam(required = false) LocalDateTime endDate
  ) {
    if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
      throw new InvalidDataException("Atât data de început, cât și data de sfârșit sunt obligatorii pentru filtrare.");
    }
    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
      throw new InvalidDataException("Data de început trebuie să fie anterioară sau egală cu data de sfârșit.");
    }

    List<Appointment> appointments = service.getOverlappingAppointments(startDate, endDate);
    return ResponseEntity.ok(appointments);
  }

  // Get Appointment by id
  @GetMapping("/{id}")
  public ResponseEntity<Appointment> getAppointmentById(@PathVariable UUID id) {
    return service.getAppointmentById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // Update an Appointment
  @PutMapping("/{id}")
  public ResponseEntity<Appointment> updateAppointment(@PathVariable UUID id, @RequestBody AppointmentRequest request) {
    try {
      // Mapăm datele din DTO în Entitate pentru update
      Appointment appointmentDetails = new Appointment();
      appointmentDetails.setUserId(request.getUserId());
      appointmentDetails.setDescription(request.getDescription());
      appointmentDetails.setStartDate(request.getStartDate());
      appointmentDetails.setEndDate(request.getEndDate());
      return ResponseEntity.ok(service.updateAppointment(id, appointmentDetails));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Delete an Appointment
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
    service.deleteAppointment(id);
    return ResponseEntity.noContent().build();
  }
}
