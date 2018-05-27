package demo.repository;

import demo.entity.Appointment;
import demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Appointment findById(int id);
    List<Appointment> findByDate(LocalDateTime date);
    List<Appointment> findByEmployee(User employee);
}