package demo.repository;

import demo.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Appointment findById(int id);
    List<Appointment> findByDate(Date date);
}