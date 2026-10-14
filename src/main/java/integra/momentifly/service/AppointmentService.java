package integra.momentifly.service;

import integra.momentifly.model.Appointment;
import integra.momentifly.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;

    @Autowired
    public AppointmentService(AppointmentRepository repository) {
        this.repository = repository;
    }

    public Appointment createAppointment(Appointment appointment) {
        return repository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return repository.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        return repository.findById(id);
    }

    public Appointment updateAppointment(UUID id, Appointment appointmentDetails) {
        return repository.findById(id).map(appointment -> {
            appointment.setUserId(appointmentDetails.getUserId());
            appointment.setDescription(appointmentDetails.getDescription());
            appointment.setStartDate(appointmentDetails.getStartDate());
            appointment.setEndDate(appointmentDetails.getEndDate());
            return repository.save(appointment);
        }).orElseThrow(() -> new RuntimeException("Appointment not found with id " + id));
    }

    public void deleteAppointment(UUID id) {
        repository.deleteById(id);
    }
}