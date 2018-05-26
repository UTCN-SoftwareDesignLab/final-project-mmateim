package demo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {

    @NotNull
    @Size(min = 4, message = "Minimum 4 letters")
    private String username;

    @NotNull
    @Size(min = 4, message = "Minimum 4 letters")
    private String password;

    @Pattern(regexp ="(^CLIENT$|^EMPLOYEE$)", message = "Role must be CLIENT / EMPLOYEE")
    private String role;

    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message ="Name Can only contain letters and .,-'")
    private String name;

    public UserDto(String username, String password, String role, String name) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    public UserDto() {
    }

    public UserDto(String role){
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
