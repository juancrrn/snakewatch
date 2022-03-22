package es.ucm.fdi.iw.controller;


import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.ucm.fdi.iw.model.Friendship;
import es.ucm.fdi.iw.model.Level;
import es.ucm.fdi.iw.model.MatchPlayer;
import es.ucm.fdi.iw.model.RoomUser;
import es.ucm.fdi.iw.model.User;

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

    @Autowired
    private EntityManager entityManager;
    
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

    /**
     * Levels view
     */
    @GetMapping("/levels")
    public String levels(Model model) {
        List<Level> levels = entityManager
            .createNamedQuery("Level.getLevels", Level.class)
            .getResultList();
        model.addAttribute("levels", levels); 
        return "levels";
    }

    /**
     * Game view
     */
    @GetMapping("/game")
    public String game(Model model){
        return "game";
    }
}
