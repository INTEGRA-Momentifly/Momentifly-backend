package integra.momentifly.controller;

import integra.momentifly.dto.AppointmentRequest;
import integra.momentifly.model.Appointment;
import integra.momentifly.service.AppointmentService;
import integra.momentifly.service.UserService;
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
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(Authentication authentication, @RequestBody AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setUser(userService.getUserByUsername(authentication.getName()));
        appointment.setDescription(request.getDescription());
        appointment.setStartDate(request.getStartDate());
        appointment.setEndDate(request.getEndDate());
        return ResponseEntity.ok(service.createAppointment(appointment));
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(service.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable UUID id) {
        return service.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping()
    public ResponseEntity<Appointment> updateAppointment(Authentication authentication, @RequestBody AppointmentRequest request) {
        try {
            return ResponseEntity.ok(service.updateAppointment(userService.getUserByUsername(authentication.getName()).getId(), request));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
        service.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}