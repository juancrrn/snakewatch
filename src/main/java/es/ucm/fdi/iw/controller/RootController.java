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
import es.ucm.fdi.iw.model.FriendshipKey;
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

        List<User> amigos = new ArrayList<User>();
        User adminUser = entityManager.createNamedQuery("User.byUsername", User.class).setParameter("username", "a").getSingleResult();
        List<User> users = entityManager.createNamedQuery("User.getUsersLessMe", User.class).setParameter("username", adminUser.getUsername()).getResultList();
    
        for(int i=0; i<users.size();i++){
            FriendshipKey fkey = new FriendshipKey(adminUser, users.get(i));
            Friendship friendship = new Friendship(fkey);
            if(entityManager.find(Friendship.class, friendship.getId()) == null) {
                entityManager.persist(friendship);
                entityManager.flush();
            }
        }

        List<Friendship> userFriendships = entityManager
                    .createNamedQuery("Friendship.getFriends", Friendship.class)
                    .setParameter("userid", adminUser.getId())
                    .getResultList();
                    
        for (Friendship friendship : userFriendships){
            if(friendship.getId().getUser1().getId() == adminUser.getId()){
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
