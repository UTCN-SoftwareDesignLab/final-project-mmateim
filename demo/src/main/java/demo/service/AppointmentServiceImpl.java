package demo.service;

import demo.dto.AppointmentDto;
import demo.entity.Appointment;
import demo.entity.Location;
import demo.entity.User;
import demo.repository.AppointmentRepository;
import demo.repository.LocationRepository;
import demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    AppointmentRepository appointmentRepository;
    LocationRepository locationRepository;
    UserRepository userRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, LocationRepository locationRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment findById(Integer id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Appointment create(AppointmentDto appointmentDto) {
        Location location = locationRepository.findById(appointmentDto.getLocation_id());
        User employee = userRepository.findById(appointmentDto.getEmployee_id());
        User client = userRepository.findById(appointmentDto.getClient_id());
        Appointment appointment = new Appointment(employee, client, appointmentDto.getDate(), location, appointmentDto.getDetails());
        return appointmentRepository.save(appointment);
    }

    @Override
    public void delete(int id) {
        appointmentRepository.delete(id);
    }

    @Override
    public Appointment update(AppointmentDto appointmentDto, Integer id) {
        Location location = locationRepository.findById(appointmentDto.getLocation_id());
        User employee = userRepository.findById(appointmentDto.getEmployee_id());
        User client = userRepository.findById(appointmentDto.getClient_id());
        Appointment appointment = new Appointment(employee, client, appointmentDto.getDate(), location, appointmentDto.getDetails());
        appointment.setId(id);
        return appointmentRepository.save(appointment);
    }

    @Override
    public boolean isEmployeeAvailable(int employee_id, LocalDateTime date) {
        User employee = userRepository.findById(employee_id);
        List<Appointment> appointments = appointmentRepository.findByEmployee(employee);
        for (Appointment a : appointments) {
            if (areClose(a.getDate(), date)) {
                return false;
            }
        }
        return true;
    }

    private boolean areClose(LocalDateTime t1, LocalDateTime t2){
        if(t1.isBefore(t2) != t1.isBefore(t2.minusHours(2))){
            return true;
        }
        else if(t1.isAfter(t2) != t1.isAfter(t2.plusHours(2))){
            return true;
        }
        return false;
    }
}
