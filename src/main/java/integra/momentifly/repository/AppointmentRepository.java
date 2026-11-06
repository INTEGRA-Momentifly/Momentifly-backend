package integra.momentifly.repository;

import integra.momentifly.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByUser_Username(String username);
    Optional<Appointment> findByIdAndUser_Username(UUID id, String username);
}