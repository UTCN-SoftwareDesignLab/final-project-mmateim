package demo.controller;

import demo.dto.AppointmentDto;
import demo.dto.Message;
import demo.entity.Appointment;
import demo.entity.Location;
import demo.entity.User;
import demo.service.AppointmentService;
import demo.service.LocationService;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Controller
@RequestMapping(value = "/appointments-client")
public class AppointmentControllerClient {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(Model model) {
        model.addAttribute("appointmentDto", new AppointmentDto());
        model.addAttribute("appointments", getOwnAppointments());
        model.addAttribute("employees", userService.getByRole("EMPLOYEE"));
        model.addAttribute("message", "");
        model.addAttribute("locations", locationService.getAll());
        System.out.println("AppointmentController : return appointments-client.html");
        return "appointments-client";
    }

    @RequestMapping(params = "create=", method = RequestMethod.POST)
    public String createAppointment(Model model, @Valid @ModelAttribute("appointmentDto") AppointmentDto appointmentDto, BindingResult bindingResult) {
        System.out.println("AppointmentController : create");
        String message;
        if (appointmentDto.getDate().toLocalDate().isAfter(LocalDate.now())) {
            if (!bindingResult.hasErrors()) {
                appointmentDto.setClient_id(userService.getCurrentUser().getId());
                if (appointmentService.isEmployeeAvailable(appointmentDto.getEmployee_id(), appointmentDto.getDate())) {
                    if (appointmentService.create(appointmentDto) != null) {
                        System.out.println("AppointmentController : create appointment Done");
                        message = "";
                        model.addAttribute("appointmentDto", new AppointmentDto());
                        sendMessage(appointmentDto);
                    } else {
                        message = "SQL error at insert";
                    }
                } else {
                    message = "Employee is not available for this date";
                }
            } else {
                message = Utilities.getErrors(bindingResult);
            }
        } else {
            message = "Date is not in the future";
        }
        if (!message.equals("")) {
            model.addAttribute("appointmentDto", appointmentDto);
            System.out.println("AppointmentController : error at create appointment");
        }
        model.addAttribute("appointments", getOwnAppointments());
        model.addAttribute("message", message);
        model.addAttribute("employees", userService.getByRole("EMPLOYEE"));
        model.addAttribute("locations", locationService.getAll());
        return "appointments-client";
    }

    @RequestMapping(params = "update=", method = RequestMethod.POST)
    public String updateAppointment(Model model, @Valid @ModelAttribute("appointmentDto") AppointmentDto appointmentDto, @NotNull @RequestParam("appointmentId") Integer appointmentId, BindingResult bindingResult) {
        System.out.println("AppointmentController : update");
        String message;
        if (appointmentDto.getDate().toLocalDate().isAfter(LocalDate.now())) {
            if (!bindingResult.hasErrors()) {
                User user = userService.getCurrentUser();
                appointmentDto.setClient_id(user.getId());
                if (appointmentService.update(appointmentDto, appointmentId) != null) {
                    System.out.println("AppointmentController : update appointment Done");
                    message = "";
                    model.addAttribute("appointmentDto", new AppointmentDto());
                } else {
                    message = "SQL error at update";
                }
            } else {
                message = Utilities.getErrors(bindingResult);
            }
        } else {
            message = "Date is not in the future";
        }
        if (!message.equals("")) {
            model.addAttribute("appointmentDto", appointmentDto);
            System.out.println("AppointmentController : error at update Appointment");
        }
        model.addAttribute("appointments", getOwnAppointments());
        model.addAttribute("message", message);
        model.addAttribute("employees", userService.getByRole("EMPLOYEE"));
        model.addAttribute("locations", locationService.getAll());
        return "appointments-client";
    }

    private Object getOwnAppointments() {
        User user = userService.getCurrentUser();
        return appointmentService.getAll().stream().filter(it -> it.getClient().equals(user)).toArray();
    }

    private void sendMessage(AppointmentDto appointmentDto){
        User client = userService.findById(appointmentDto.getClient_id());
        Location location = locationService.findById(appointmentDto.getLocation_id());
        Message message1 = new Message("Client " + client.getName() + " scheduled an appointment on " + appointmentDto.getDate() + " at " + location.getName());
        User employee = userService.findById(appointmentDto.getEmployee_id());
        simpMessagingTemplate.convertAndSendToUser(employee.getUsername(), "/queue/reply", message1);
    }
}
