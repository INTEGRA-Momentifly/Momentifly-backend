package integra.momentifly.service;

import integra.momentifly.dto.AppointmentRequest;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.Appointment;
import integra.momentifly.repository.AppointmentRepository;
import integra.momentifly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repository;
    private final UserService userService;

    public Appointment createAppointment(String username, AppointmentRequest request) {
        Appointment appointment = new Appointment();
        appointment.setUser(userService.getUserByUsername(username));
        appointment.setDescription(request.getDescription());
        appointment.setStartDate(request.getStartDate());
        appointment.setEndDate(request.getEndDate());
        return repository.save(appointment);
    }

    public List<Appointment> getAppointmentsForUser(String username) {
        return repository.findByUser_Username(username);
    }

    public Appointment getAppointmentById(UUID id, String username) {
        return repository.findByIdAndUser_Username(id, username)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));
    }

    public Appointment updateAppointment(UUID id, String username, AppointmentRequest request) {
        Appointment appointment = getAppointmentById(id, username);
        appointment.setDescription(request.getDescription());
        appointment.setStartDate(request.getStartDate());
        appointment.setEndDate(request.getEndDate());
        return repository.save(appointment);
    }

    public void deleteAppointment(UUID id, String username) {
        repository.delete(getAppointmentById(id, username));
    }
}