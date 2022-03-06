package es.ucm.fdi.iw.controller;


import java.util.ArrayList;
import java.util.List;


import javax.persistence.EntityManager;
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
    @Autowired
	private PasswordEncoder passwordEncoder;
    
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
    public String profile(Model model) {
        User thisUser = (User) session.getAttribute("u");
        
        User user2 = new User();
        //user2.setPassword(passwordEncoder.encode(UserController.generateRandomBase64Token(12)));
        user2.setPassword("patatas");
        user2.setEnabled(true);
        user2.setUsername("OscarM");
        user2.setFirstName("Oscar");
        user2.setLastName("Molano");

        User user3 = new User();
        //user3.setPassword(passwordEncoder.encode(UserController.generateRandomBase64Token(12)));
        user3.setPassword("patatas");
        user3.setEnabled(true);
        user3.setUsername("OscarC");
        user3.setFirstName("Oscar");
        user3.setLastName("Caro");
    
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();
        

        Friendship fr2 = new Friendship(thisUser, user2);
        Friendship fr3 = new Friendship(thisUser, user3);
        
        entityManager.persist(fr2);
        entityManager.persist(fr3);
        entityManager.flush();

        List<User> amigos = new ArrayList<User>();

        List<Friendship> userFriendships = entityManager
                    .createNamedQuery("Friendship.getFriends", Friendship.class)
                    .setParameter("userid", thisUser.getId())
                    .getResultList();
                    
        for (Friendship friendship : userFriendships){
            if(friendship.getId().getUser1().getId() == thisUser.getId()){
                amigos.add(friendship.getId().getUser2());
            }
            else{
                amigos.add(friendship.getId().getUser1());
            }
        }
        
        model.addAttribute("amigos", amigos);
        return "profile";
    }

    /**
     * Levels view
     */
    @GetMapping("/levels")
    public String levels(Model model) {
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
