package demo.service;

import demo.dto.AppointmentDto;
import demo.entity.Appointment;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AppointmentService {
    List<Appointment> getAll();
    Appointment findById(Integer id);
    Appointment create(AppointmentDto appointmentDto);
    void delete(int id);
    Appointment update(AppointmentDto appointmentDto, Integer id);
    boolean isEmployeeAvailable(int employee_id, LocalDateTime date);
}
