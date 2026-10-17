package integra.momentifly.repository;

import integra.momentifly.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

  List<Appointment> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);
  //Nu imi recunoaste metoda fara sa o implementez in repo
}
