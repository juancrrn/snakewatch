package es.ucm.fdi.iw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Root controller
 * 
 * Non-authenticated requests only.
 * 
 * @author Daniel Marín Irún
 * @author Juan Carrión Molina
 * @author Mohamed Ghanem
 * @author Óscar Caro Navarro
 * @author Óscar Molano Buitrago
 * 
 * @version 0.0.1
 */
@Controller
public class RootController {


    /**
     * Home view
     */
    @GetMapping("/")
    public String lobby(Model model) {
        return "lobby";
    }

    /**
     * Login view
     */
    @GetMapping("/login")
    public String login(Model model) {    
        return "login";
    }

    @GetMapping("/login/failed")
    public String postLoginFailed(RedirectAttributes attributes)
        throws IllegalArgumentException{  
        attributes.addFlashAttribute("message", "Invalid credentials.");
        return "redirect:/login";
    }

}
