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

    public Appointment createAppointment(Appointment appointment) {
        return repository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return repository.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        return repository.findById(id);
    }

    public Appointment updateAppointment(UUID id, AppointmentRequest request) {
        return repository.findById(id).map(appointment -> {
            appointment.setDescription(request.getDescription());
            appointment.setStartDate(request.getStartDate());
            appointment.setEndDate(request.getEndDate());
            return repository.save(appointment);
        }).orElseThrow(() -> new NotFoundException("Appointment not found with id " + id));
    }

    public void deleteAppointment(UUID id) {
        repository.deleteById(id);
    }
}