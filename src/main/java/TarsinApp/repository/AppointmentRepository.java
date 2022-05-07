package TarsinApp.repository;

import TarsinApp.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    public List<Appointment> findByDateGoing(LocalDate dateGoing);
    public List<Appointment> findByGoTo(String goingTo);
    public List<Appointment> findByStartFrom(String startFrom);
}
