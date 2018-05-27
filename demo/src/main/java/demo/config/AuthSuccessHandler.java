package demo.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        // Get the role of logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();

        if (role.contains("ADMIN")) {
            System.out.println("Handler : Logged in as ADMIN");
            return "/users";
        }
        if (role.contains("EMPLOYEE")) {
            System.out.println("Handler : Logged in as SECRETARY");
            return "/appointments-employee";
        }
        System.out.println("Handler : Logged in as CLIENT");
        return "/profile";
    }
}