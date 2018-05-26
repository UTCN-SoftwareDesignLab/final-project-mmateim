package demo.controller;

import demo.dto.UserDto;
import demo.entity.User;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.sql.Array;
import java.util.List;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService service;

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(Model model) {
        model.addAttribute("message", "");
        model.addAttribute("users", getRegularUsers());
        model.addAttribute("userDto", new UserDto());
        System.out.println("UserController : return users-admin.html");
        return "users-admin";
    }

    @RequestMapping(params = "create=", method = RequestMethod.POST)
    public String createUser(Model model, @Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult) {
        System.out.println("UserController : create");
        String message;
        if (!bindingResult.hasErrors()) {
            if(service.create(userDto) != null) {
                System.out.println("UserController : create user Done");
                message = "";
                model.addAttribute("userDto", new UserDto());
            }
            else {
               message = "SQL error at insert";
            }
        } else {
            message = getErrors(bindingResult);
        }
        if(!message.equals("")){
            model.addAttribute("userDto", userDto);
            System.out.println("UserController : error at create user");
        }
        model.addAttribute("message", message);
        model.addAttribute("users", getRegularUsers());
        return "users-admin";
    }

    @RequestMapping(params = "update=", method = RequestMethod.POST)
    public String updateUser(Model model, @Valid @ModelAttribute("userDto") UserDto userDto, @NotNull @RequestParam("userId") Integer userId, BindingResult bindingResult) {
        System.out.println("UserController : update");
        String message;
        if (!bindingResult.hasErrors()) {
            if(service.update(userDto, userId) != null) {
                System.out.println("UserController : update user Done");
                message = "";
                model.addAttribute("userDto", new UserDto());
            }
            else {
                message = "SQL error at update";
            }
        } else {
            message = getErrors(bindingResult);
        }
        if(!message.equals("")){
            model.addAttribute("userDto", userDto);
            System.out.println("UserController : error at update user");
        }
        model.addAttribute("message", message);
        model.addAttribute("users", getRegularUsers());
        return "users-admin";
    }

    @RequestMapping(params = "delete=", method = RequestMethod.GET)
    public String deleteUser(Model model, @RequestParam("deleteId") Integer deleteId) {
        System.out.println("UserController : delete");
        String message = "";
        if (deleteId != null) {
            service.delete(deleteId);
        } else {
            message = "Id field is empty";
        }
        model.addAttribute("userDto", new UserDto());
        if(message.equals("")){
            model.addAttribute("user", new User());
            return "login";
        }
        else {
            model.addAttribute("users", getRegularUsers());
            model.addAttribute("message", message);
            return "books-admin";
        }
    }

    private String getErrors(BindingResult bindingResult){
        String message = "";
        List<ObjectError> errors = bindingResult.getAllErrors();
        for( ObjectError e : errors){
            message += "ERROR: " + e.getDefaultMessage();
        }
        return message;
    }

    private Object[] getRegularUsers(){
        return service.getAll().stream().filter(u-> !u.getRole().equals("ADMIN")).toArray();
    }
}
