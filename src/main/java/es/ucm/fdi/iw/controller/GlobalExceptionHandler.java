package es.ucm.fdi.iw.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @Autowired HttpSession session;

  
    @ExceptionHandler(value= IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, RedirectAttributes attributes){
        String path = session.getServletContext().getContextPath();
        attributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/" + path;
    }
}
