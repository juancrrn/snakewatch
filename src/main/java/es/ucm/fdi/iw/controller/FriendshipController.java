package es.ucm.fdi.iw.controller;

import es.ucm.fdi.iw.model.Friendship;
import es.ucm.fdi.iw.model.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import java.util.ArrayList;

import java.util.List;

import org.json.simple.*;

/**
 * User management
 * 
 * Authentication is required to access this endpoint.
 * 
 * @author Daniel Marín Irún
 * @author Juan Carrión Molina
 * @author Mohamed Ghanem
 * @author Óscar Caro Navarro
 * @author Óscar Molano Buitrago
 * 
 * @version 0.0.1
 */
@Controller()
@RequestMapping("friendship")
public class FriendshipController {

    private static final Logger log = LogManager.getLogger(FriendshipController.class);

    @Autowired
    private EntityManager entityManager;

    /**
     * Exception to use when denying access to unauthorized users.
     * 
     * In general, admins are always authorized, but users cannot modify each
     * other's profiles.
     */
    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "You are not an admin an this profile is not your own.")
    public static class NotOwnProfileException extends RuntimeException {
    }

    /**
     * Check the state of friendship between the current user and other user.
     *
     * FIXME "state" should probably be "status"
     *
     * @param otherUser the user to check friendship status with
     * @param model
     * @param sesion    current session used for this request
     * @return status of friendship between both users
     */
    @PostMapping("/state/{otherUserId}")
    @ResponseBody
    @Transactional
    public String getFriendStatus(@PathVariable long otherUserId, Model model, HttpSession session) {
        User loggedUser = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());
        User otherUser = entityManager.find(User.class, otherUserId);

        boolean isFriends = false;
        boolean requestSent = false;
        boolean requestReceived = false;

        List<Friendship> acceptedFr = new ArrayList<>(loggedUser.getFriendships());
        acceptedFr.removeIf(
                f -> (f.getStatus() != Friendship.Status.ACCEPTED || f.getUser2().getId() != otherUser.getId()));
        if (!acceptedFr.isEmpty()) {
            isFriends = true;
        }

        List<Friendship> pendingFr = new ArrayList<>(loggedUser.getFriendships());
        pendingFr.removeIf(
                f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != otherUser.getId()));
        if (!pendingFr.isEmpty()) {
            requestSent = true;
        }

        List<Friendship> pendingReceivedFr = new ArrayList<>(otherUser.getFriendships());
        pendingReceivedFr.removeIf(
                f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != loggedUser.getId()));
        if (!pendingReceivedFr.isEmpty()) {
            requestReceived = true;
        }

        JSONObject json = new JSONObject();

        // FIXME Use a switch-case statement
        if (isFriends) {
            json.put("result", "friends");
        } else if (requestSent) {
            json.put("result", "request sent");
        } else if (requestReceived) {
            json.put("result", "request received");
        } else {
            json.put("result", "not friends");
        }

        return json.toString();
    }

    /**
     * Send a friend request from current user to other user
     *
     * @param otherUser the user to send the request to
     * @param model
     * @param sesion    current session used for this request
     * @return request result status
     */
    // FIXME Change path to "/requests/{recipientId}/send/"
    @PostMapping("/send_req/{otherUserId}")
    @ResponseBody
    @Transactional
    public String sendFriendReq(@PathVariable long otherUserId, Model model, HttpSession session) {
        User loggedUser = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());
        User receiver = entityManager.find(User.class, otherUserId);

        // TODO If Friendship's default status is PENDING, put that in the constructor.
        Friendship friendship = new Friendship();
        friendship.setUser1(loggedUser);
        friendship.setUser2(receiver);
        friendship.setStatus(Friendship.Status.PENDING);
        entityManager.persist(friendship);
        entityManager.flush();

        // TODO Remove if not needed
        log.info("Sending a friend request from '{}' to '{}'", loggedUser.getUsername(), receiver.getUsername());

        // FIXME Create a JSONObject and transform it to string
        return "{\"result\": \"ok\"}";
    }

    /**
     * Accept the friend request sent from other user to current user
     * 
     * @param otherUserId the user who sent the request
     * @param model
     * @param session     current session used for this request
     * @return request result status
     */
    // FIXME Change path to "/requests/{senderId}/accept/"
    @PostMapping("/accept_req/{otherUserId}/")
    @ResponseBody
    @Transactional
    public String acceptFriendReq(@PathVariable long otherUserId, Model model, HttpSession session) {
        User loggedUser = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());
        User sender = entityManager.find(User.class, otherUserId);

        List<Friendship> frList = new ArrayList<>(sender.getFriendships());
        frList.removeIf(
                f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != loggedUser.getId()));

        Long frId = frList.get(0).getId();
        Friendship frRequest = entityManager.find(Friendship.class, frId);

        // 1º Actualizar la friendship request a ACCEPTED
        frRequest.setStatus(Friendship.Status.ACCEPTED);
        entityManager.merge(frRequest);

        // 2º Crear la instancia reversa de friendship
        Friendship fr2 = new Friendship();
        fr2.setUser1(loggedUser);
        fr2.setUser2(sender);
        fr2.setStatus(Friendship.Status.ACCEPTED);
        entityManager.persist(fr2);
        loggedUser.getFriendships().add(fr2);
        entityManager.merge(sender);
        entityManager.merge(loggedUser);

        entityManager.flush();

        // Registrar accion en logs
        log.info("Accepting the friend request from '{}' to '{}'", sender.getUsername(), loggedUser.getUsername());

        // FIXME Create a JSONObject and transform it to string
        return "{\"result\": \"ok\"}";
    }

    /**
     * Reject the friend request sent from other user to current user
     * 
     * @param otherUser the user who sent the request
     * @param model
     * @param session   current session used for this request
     * @return request result status
     */
    // FIXME Change path to "/requests/{senderId}/reject/"
    @PostMapping("/reject_req/{otherUserId}/")
    @ResponseBody
    @Transactional
    public String rejectFriendReq(@PathVariable long otherUserId, Model model, HttpSession session) {
        User loggedUser = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());
        User sender = entityManager.find(User.class, otherUserId);

        List<Friendship> frList = new ArrayList<>(sender.getFriendships());
        frList.removeIf(
                f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != loggedUser.getId()));

        Long frId = frList.get(0).getId();
        Friendship frRequest = entityManager.find(Friendship.class, frId);

        // 1º Eliminar la request de la bbdd
        entityManager.remove(frRequest);
        sender.getFriendships().remove(frRequest);
        entityManager.merge(sender);
        entityManager.flush();

        // FIXME Create a JSONObject and transform it to string
        return "{\"result\": \"friend request rejected.\"}";
    }

    /**
     * Cancel the friend request sent from current user to other user
     * 
     * @param otherUserId the user to whom the request was sent
     * @param model
     * @param session     current session used for this request
     * @return request result status
     */
    // FIXME Change path to "/requests/{recipientId}/cancel/"
    @PostMapping("/cancel_req/{otherUserId}")
    @ResponseBody
    @Transactional
    public String cancelFriendReq(@PathVariable long otherUserId, Model model, HttpSession session) {
        User loggedUser = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());

        List<Friendship> frList = new ArrayList<>(loggedUser.getFriendships());
        frList.removeIf(f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != otherUserId));

        Long frId = frList.get(0).getId();
        Friendship frRequest = entityManager.find(Friendship.class, frId);

        // 1º Eliminar la request de la bbdd
        entityManager.remove(frRequest);
        loggedUser.getFriendships().remove(frRequest);
        entityManager.merge(loggedUser);
        entityManager.flush();

        // FIXME Create a JSONObject and transform it to string
        return "{\"result\": \"friend request canceled.\"}";
    }

    /**
     * Removes the friendship between current user and other user
     *
     * @param otherUserId the user to unfriend
     * @param model
     * @param session     current session used for this request
     * @return request result status
     */
    // FIXME Change path to "/requests/{friendId}/delete/"
    @PostMapping("/finish/{otherUserId}")
    @ResponseBody
    @Transactional
    public String finishFriendship(@PathVariable long otherUserId, Model model, HttpSession session) {
        User loggedInUser = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());
        User friend = entityManager.find(User.class, otherUserId);

        List<Friendship> loggedInUserFriendships = new ArrayList<>(loggedInUser.getFriendships());
        /**
         * FIXME This seems obvious but if you have to retrieve an entity it is
         * bad if you retrieve them all and then remove the ones you do not want
         * or need
         */
        loggedInUserFriendships.removeIf(f -> (f.getUser2().getId() != friend.getId()));
        // FIXME Better to store the friendship and then get the id where needed
        Long loggedInUserFriendshipId = loggedInUserFriendships.get(0).getId();

        List<Friendship> friendFriendships = new ArrayList<>(friend.getFriendships());
        friendFriendships.removeIf(f -> (f.getUser2().getId() != loggedInUser.getId()));
        Long friendFriendshipId = friendFriendships.get(0).getId();

        // FIXME Haven't they been already retrieved?
        Friendship fr1 = entityManager.find(Friendship.class, loggedInUserFriendshipId);
        Friendship fr2 = entityManager.find(Friendship.class, friendFriendshipId);
        entityManager.remove(fr1);
        entityManager.remove(fr2);

        entityManager.remove(fr1);
        entityManager.remove(fr2);
        loggedInUser.getFriendships().remove(fr1);
        friend.getFriendships().remove(fr2);
        entityManager.merge(loggedInUser);
        entityManager.merge(friend);
        entityManager.flush();

        // FIXME Remove if not required
        log.info("Removing friendship between '{}' to '{}'", loggedInUser.getUsername(), friend.getUsername());

        // FIXME Create a JSONObject and transform it to string
        return "{\"result\": \"friendship finished.\"}";
    }
}