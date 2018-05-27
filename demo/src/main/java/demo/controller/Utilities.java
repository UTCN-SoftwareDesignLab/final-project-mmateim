package demo.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

public class Utilities {

    public static String getErrors(BindingResult bindingResult) {
        String message = "";
        List<ObjectError> errors = bindingResult.getAllErrors();
        for (ObjectError e : errors) {
            message += "ERROR: " + e.getDefaultMessage();
        }
        return message;
    }
}
