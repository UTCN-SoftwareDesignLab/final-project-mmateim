package demo.controller;

import demo.dto.AppointmentDto;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = "/appointments-employee")
public class AppointmentControllerEmployee {

    private AppointmentService appointmentService;
    private UserService userService;
    private LocationService locationService;

    @Autowired
    public AppointmentControllerEmployee(AppointmentService appointmentService, UserService userService, LocationService locationService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.locationService = locationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(Model model) {
        model.addAttribute("appointments", getOwnAppointments());
        model.addAttribute("message", "");
        model.addAttribute("clients", userService.getByRole("CLIENT"));
        model.addAttribute("locations", locationService.getAll());
        model.addAttribute("appointmentDto", new AppointmentDto());
        System.out.println("AppointmentController : return appointments-employee.html");
        return "appointments-employee";
    }

    @RequestMapping(params = "create=", method = RequestMethod.POST)
    public String createAppointment(Model model, @Valid @ModelAttribute("appointmentDto") AppointmentDto appointmentDto, BindingResult bindingResult) {
        System.out.println("AppointmentController : create");
        String message;
        if(appointmentDto.getDate().toLocalDate().isAfter(LocalDate.now())) {
            if (!bindingResult.hasErrors()) {
                appointmentDto.setEmployee_id(userService.getCurrentUser().getId());
                if (appointmentService.isEmployeeAvailable(appointmentDto.getEmployee_id(), appointmentDto.getDate())) {
                    if (appointmentService.create(appointmentDto) != null) {
                        System.out.println("AppointmentController : create appointment Done");
                        message = "";
                        model.addAttribute("appointmentDto", new AppointmentDto());

                    /*
                    Message message1 = new Message("Client " + appointmentDto.getClient_id() + " scheduled a consultation on " + appointmentDto.getDate());
                    User employee = userService.findById(appointmentDto.getEmployee_id());
                    simpMessagingTemplate.convertAndSendToUser(employee.getUsername(), "/queue/reply", message1);
                    */
                    } else {
                        message = "SQL error at insert";
                    }
                } else {
                    message = "Employee is not available for this date";
                }
            } else {
                message = Utilities.getErrors(bindingResult);
            }
        }
        else {
            message = "Date is not in the future";
        }
        if (!message.equals("")) {
            model.addAttribute("appointmentDto", appointmentDto);
            System.out.println("AppointmentController : error at create appointment");
        }
        model.addAttribute("appointments", getOwnAppointments());
        model.addAttribute("message", message);
        model.addAttribute("clients", userService.getByRole("CLIENT"));
        model.addAttribute("locations", locationService.getAll());
        return "appointments-employee";
    }

    @RequestMapping(params = "update=", method = RequestMethod.POST)
    public String updateAppointment(Model model, @Valid @ModelAttribute("appointmentDto") AppointmentDto appointmentDto, @NotNull @RequestParam("appointmentId") Integer appointmentId, BindingResult bindingResult) {
        System.out.println("AppointmentController : update");
        String message;
        if(appointmentDto.getDate().toLocalDate().isAfter(LocalDate.now())) {
            if (!bindingResult.hasErrors()) {
                User user = userService.getCurrentUser();
                appointmentDto.setEmployee_id(user.getId());
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
        }
        else {
            message = "Date is not in the future";
        }
        if (!message.equals("")) {
            model.addAttribute("appointmentDto", appointmentDto);
            System.out.println("AppointmentController : error at update Appointment");
        }
        model.addAttribute("appointments", getOwnAppointments());
        model.addAttribute("message", message);
        model.addAttribute("clients", userService.getByRole("CLIENT"));
        model.addAttribute("locations", locationService.getAll());
        return "appointments-employee";
    }

    @RequestMapping(params = "delete=", method = RequestMethod.GET)
    public String deleteAppointment(Model model, @RequestParam("deleteId") Integer deleteId) {
        System.out.println("AppointmentController : delete");
        String message = "";
        if (deleteId != null) {
            appointmentService.delete(deleteId);
        } else {
            message = "Id field is empty";
        }
        model.addAttribute("appointmentDto", new AppointmentDto());
        model.addAttribute("appointments", getOwnAppointments());
        model.addAttribute("message", message);
        model.addAttribute("clients", userService.getByRole("CLIENT"));
        model.addAttribute("locations", locationService.getAll());
        return "appointments-employee";
    }

    private Object[] getOwnAppointments(){
        User user = userService.getCurrentUser();
        return appointmentService.getAll().stream().filter(a->a.getEmployee() == user).toArray();
    }
}
