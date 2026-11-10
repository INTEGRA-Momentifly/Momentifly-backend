package integra.momentifly.controller;

import integra.momentifly.dto.AppointmentRequest;
import integra.momentifly.model.Appointment;
import integra.momentifly.service.AppointmentService;
import integra.momentifly.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(Authentication authentication,
                                                         @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(service.createAppointment(authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments(Authentication authentication) {
        return ResponseEntity.ok(service.getAppointmentsForUser(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(Authentication authentication, @PathVariable UUID id) {
        return ResponseEntity.ok(service.getAppointmentById(id, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(Authentication authentication, @PathVariable UUID id,
                                                         @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(service.updateAppointment(id, authentication.getName(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(Authentication authentication, @PathVariable UUID id) {
        service.deleteAppointment(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}