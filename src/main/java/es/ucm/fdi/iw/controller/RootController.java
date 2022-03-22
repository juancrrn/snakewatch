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
    private HttpSession session;

	//private static final Logger log = LogManager.getLogger(RootController.class);

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
     * Profile view
     */
    @GetMapping("/profile")
    public String profile(HttpServletRequest request, HttpServletResponse response, Model model) {

        // Get logged user
        User user = entityManager.find(User.class, ((User)session.getAttribute("u")).getId());
        model.addAttribute("user", user);
        
        // Get their friendships
        List<Friendship> friendships = user.getFriendships();
        // Remove the ones that are not on ACCEPTED status
        //friendships.removeIf(f -> (f.getStatus() != Friendship.Status.ACCEPTED));
        model.addAttribute("friendships", friendships);
        
        // Get their friend requests (any sender, this user as receiver, status as "Pending")
        List<Friendship> friendRequests = entityManager.createNamedQuery("Friendship.getRequests", Friendship.class)
            .setParameter("userId", user.getId())
            .getResultList();
        model.addAttribute("friendRequests", friendRequests);        

        // Get matches that this user has played (as MatchPlayer objects)
        List<MatchPlayer> matchPlayers = user.getMatchPlayers();
        model.addAttribute("matchPlayers", matchPlayers);        
        
        // Get rooms that this user has joined (as RoomUser objects)
        List<RoomUser> roomUsers = user.getRoomUsers();
        model.addAttribute("roomUsers", roomUsers);  

        return "profile";
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
