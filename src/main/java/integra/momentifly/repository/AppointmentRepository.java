package integra.momentifly.repository;

import integra.momentifly.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

  List<Appointment> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

  @Query("SELECT a FROM Appointment a WHERE a.startDate <= :endDate AND a.endDate >= :startDate")
  List<Appointment> findOverlappingAppointments(
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate
      );
}
