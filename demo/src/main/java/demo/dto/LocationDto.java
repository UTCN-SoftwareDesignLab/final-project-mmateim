package demo.dto;

import demo.entity.LocationType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class LocationDto {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = "Name Can only contain letters and .,-'")
    private String name;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9., ]+", message = "Address can contain only letters, digits, .,")
    private String address;

    @NotNull
    @Pattern(regexp = "(^SPA$|^GYM|^SALON$)", message = "Type must be SPA / GYM / SALON")
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
