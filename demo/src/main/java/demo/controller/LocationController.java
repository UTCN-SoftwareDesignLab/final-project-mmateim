package demo.controller;

import demo.dto.LocationDto;
import demo.dto.UserDto;
import demo.entity.User;
import demo.service.LocationService;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(value = "/locations")
public class LocationController {

    @Autowired
    private LocationService service;

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(Model model) {
        model.addAttribute("message", "");
        model.addAttribute("locations", service.getAll());
        model.addAttribute("locationDto", new LocationDto());
        System.out.println("LocationController : return locations.html");
        return "locations";
    }

    @RequestMapping(params = "create=", method = RequestMethod.POST)
    public String createLocation(Model model, @Valid @ModelAttribute("locationDto") LocationDto locationDto, BindingResult bindingResult) {
        System.out.println("LocationController : create");
        String message;
        if (!bindingResult.hasErrors()) {
            if (service.create(locationDto) != null) {
                System.out.println("LocationController : create location Done");
                message = "";
                model.addAttribute("locationDto", new LocationDto());
            } else {
                message = "SQL error at insert";
            }
        } else {
            message = Utilities.getErrors(bindingResult);
        }
        if (!message.equals("")) {
            model.addAttribute("locationDto", locationDto);
            System.out.println("LocationController : error at create location");
        }
        model.addAttribute("message", message);
        model.addAttribute("locations", service.getAll());
        return "locations";
    }

    @RequestMapping(params = "update=", method = RequestMethod.POST)
    public String updateLocation(Model model, @Valid @ModelAttribute("locationDto") LocationDto locationDto, @NotNull @RequestParam("locationId") Integer locationId, BindingResult bindingResult) {
        System.out.println("LocationController : update");
        String message;
        if (!bindingResult.hasErrors()) {
            if (service.update(locationDto, locationId) != null) {
                System.out.println("LocationController : update location Done");
                message = "";
                model.addAttribute("locationDto", new LocationDto());
            } else {
                message = "SQL error at update";
            }
        } else {
            message = Utilities.getErrors(bindingResult);
        }
        if (!message.equals("")) {
            model.addAttribute("locationDto", locationDto);
            System.out.println("LocationController : error at update location");
        }
        model.addAttribute("message", message);
        model.addAttribute("locations", service.getAll());
        return "locations";
    }

    @RequestMapping(params = "delete=", method = RequestMethod.GET)
    public String deleteLocation(Model model, @RequestParam("deleteId") Integer deleteId) {
        System.out.println("LocationController : delete");
        String message = "";
        if (deleteId != null) {
            service.delete(deleteId);
        } else {
            message = "Id field is empty";
        }
        model.addAttribute("locationDto", new LocationDto());
        model.addAttribute("locations", service.getAll());
        model.addAttribute("message", message);
        return "locations";
    }
}