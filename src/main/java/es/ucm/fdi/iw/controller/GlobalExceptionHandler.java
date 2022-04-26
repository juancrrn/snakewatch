package es.ucm.fdi.iw.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(IOException.class)
    public String handleIO(IOException e, RedirectAttributes attributes){
        attributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/levels";
    }
}
