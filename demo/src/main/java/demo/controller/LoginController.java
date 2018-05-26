package demo.controller;

import demo.dto.UserDto;
import demo.entity.User;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/login", "/"}, method = RequestMethod.GET)
    public String index(Model model) {
        System.out.println("LoginController : return login.html");
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        System.out.println("LoginController : return client-form.html");
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("message", "");
        return "client-form";
    }

    @RequestMapping(value = "/login/register", method = RequestMethod.POST)
    public String registerComplete(Model model, @Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            userDto.setRole("CLIENT");
            if(userService.create(userDto) != null) {
                model.addAttribute("user", new User());
                System.out.println("LoginController : registration Done");
                return "login";
            }
            else {
                model.addAttribute("message", "SQL error at insert");
            }
        } else {
            model.addAttribute("message",bindingResult.getAllErrors());
        }
        System.out.println("LoginController : error at registration");
        model.addAttribute("userDto", userDto);
        return "user-form";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String onError(Model model){
        System.out.println("Error");
        return "error";
    }
}