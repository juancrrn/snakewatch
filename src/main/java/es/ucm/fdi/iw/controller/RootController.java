package es.ucm.fdi.iw.controller;


import java.util.ArrayList;
import java.util.List;


import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.ucm.fdi.iw.model.Friendship;
import es.ucm.fdi.iw.model.Level;
import es.ucm.fdi.iw.model.FriendshipKey;
import es.ucm.fdi.iw.model.MatchPlayer;
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

	private static final Logger log = LogManager.getLogger(RootController.class);
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
    @Transactional
    public String profile(HttpServletRequest request, HttpServletResponse response, Model model) {

        // Get logged user
        User user = (User)session.getAttribute("u");

        // Pass username with thymeleaf to the view 
        model.addAttribute("username", user.getUsername());
        
        // Get their friendships
        List<Friendship> friendships = entityManager
            .createNamedQuery("Friendship.getFriends", Friendship.class)
            .setParameter("userid", user.getId())
            .getResultList();

        // Get friends (as Users)
        List<User> amigos = new ArrayList<>();        
        for (Friendship friendship : friendships){
            if(friendship.getId().getUser1().getId() == user.getId()){
                amigos.add(friendship.getId().getUser2());
            }
            else{
                amigos.add(friendship.getId().getUser1());
            }
        }

        // Pass friends with thymeleaf to the view 
        model.addAttribute("amigos", amigos);

        
        // Get info of matches that this user has played
        List<MatchPlayer> matchPlayerList = entityManager
            .createNamedQuery("MatchPlayer.byPlayerId", MatchPlayer.class)
            .setParameter("playerId", user.getId())
            .getResultList();

        // Pass matches with thymeleaf to the view 
        model.addAttribute("matches", matchPlayerList);  
              

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

    /**
     * Rankings view
     */
    @GetMapping("/rankings")
    public String rankings(Model model){
        return "rankings";
    }
}
