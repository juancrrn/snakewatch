package es.ucm.fdi.iw.controller;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Friendship;
//import es.ucm.fdi.iw.model.Transferable;
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
//import java.util.List;
//import java.util.stream.Collectors;
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

  @Autowired
  private LocalData localData;

  @Autowired 
  private HttpSession session;

  /**
   * Exception to use when denying access to unauthorized users.
   * 
   * In general, admins are always authorized, but users cannot modify
   * each other's profiles.
   */
  @ResponseStatus(
    value=HttpStatus.FORBIDDEN, 
    reason="No eres administrador, y éste no es tu perfil")  // 403
  public static class NoEsTuPerfilException extends RuntimeException {}


  /**
   * Check the state of friendship between the current user and other user
   * @param otherUser the user to check friendship status with
   * @param model
   * @param sesion current session used for this request
   * @return status of friendship between both users
   */
  @PostMapping("/state/{otherUserId}")
  @ResponseBody
  @Transactional
  public String getFriendStatus(@PathVariable long otherUserId, Model model, HttpSession session) {
    User loggedUser = entityManager.find(User.class, ((User)session.getAttribute("u")).getId());
    User otherUser = entityManager.find(User.class, otherUserId);
    
    boolean isFriends = false;
    boolean requestSent = false;
    boolean requestReceived = false;

    List<Friendship> acceptedFr = new ArrayList<>(loggedUser.getFriendships());
    acceptedFr.removeIf(f -> (f.getStatus() != Friendship.Status.ACCEPTED || f.getUser2().getId() != otherUser.getId()));
    if (!acceptedFr.isEmpty()) {
      isFriends = true;
    }

    List<Friendship> pendingFr = new ArrayList<>(loggedUser.getFriendships());
    pendingFr.removeIf(f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != otherUser.getId()));
    if (!pendingFr.isEmpty()) {
      requestSent = true;
    }

    List<Friendship> pendingReceivedFr = new ArrayList<>(otherUser.getFriendships());
    pendingReceivedFr.removeIf(f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != loggedUser.getId()));
    if (!pendingReceivedFr.isEmpty()) {
      requestReceived = true;
    }

    JSONObject json = new JSONObject();

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
   * @param otherUser the user to send the request to
   * @param model
   * @param sesion current session used for this request
   * @return request result status
   */
  @PostMapping("/send_req/{otherUserId}")
  @ResponseBody
  @Transactional
  public String sendFriendReq(@PathVariable long otherUserId, Model model, HttpSession session) {
    User loggedUser = entityManager.find(User.class, ((User)session.getAttribute("u")).getId());
    User receiver = entityManager.find(User.class, otherUserId);

    // Contruye objeto Friendship y lo guarda en la BD
    // El estado será PENDING, hasta que el otro usuario la acepte/rechaze
    Friendship fr = new Friendship();
    fr.setUser1(loggedUser);
    fr.setUser2(receiver);
    fr.setStatus(Friendship.Status.PENDING);
    entityManager.persist(fr);
    entityManager.flush();
    
    // Registrar accion en logs    
    log.info("Sending a friend request from '{}' to '{}'", loggedUser.getUsername(), receiver.getUsername());

    return "{\"result\": \"ok\"}";
  }  

  /**
   * Accept the friend request sent from other user to current user
   * @param otherUserId the user who sent the request
   * @param model
   * @param session current session used for this request
   * @return request result status
   */
  @PostMapping("/accept_req/{otherUserId}/")
  @ResponseBody
  @Transactional
  public String acceptFriendReq(@PathVariable long otherUserId, Model model, HttpSession session) {
    User loggedUser = entityManager.find(User.class, ((User)session.getAttribute("u")).getId());
    User sender = entityManager.find(User.class, otherUserId);

    List<Friendship> frList = new ArrayList<>(sender.getFriendships());
    frList.removeIf(f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != loggedUser.getId()));
    
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

    return "{\"result\": \"friend request sent.\"}";
  }  

  /**
   * Reject the friend request sent from other user to current user
   * @param otherUser the user who sent the request
   * @param model
   * @param session current session used for this request
   * @return request result status
   */
  @PostMapping("/reject_req/{otherUserId}/")
  @ResponseBody
  @Transactional
  public String rejectFriendReq(@PathVariable long otherUserId, Model model, HttpSession session) {
    User loggedUser = entityManager.find(User.class, ((User)session.getAttribute("u")).getId());
    User sender = entityManager.find(User.class, otherUserId);

    List<Friendship> frList = new ArrayList<>(sender.getFriendships());
    frList.removeIf(f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != loggedUser.getId()));
    
    Long frId = frList.get(0).getId();
    Friendship frRequest = entityManager.find(Friendship.class, frId);

    // 1º Eliminar la request de la bbdd
    entityManager.remove(frRequest);
    sender.getFriendships().remove(frRequest);
    entityManager.merge(sender);
    entityManager.flush();

    return "{\"result\": \"friend request rejected.\"}";
  }  

  /**
   * Cancel the friend request sent from current user to other user
   * @param otherUserId the user to whom the request was sent
   * @param model
   * @param session current session used for this request
   * @return request result status
   */
  @PostMapping("/cancel_req/{otherUserId}")
  @ResponseBody
  @Transactional
  public String cancelFriendReq(@PathVariable long otherUserId, Model model, HttpSession session) {
    User loggedUser = entityManager.find(User.class, ((User)session.getAttribute("u")).getId());

    List<Friendship> frList = new ArrayList<>(loggedUser.getFriendships());
    frList.removeIf(f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != otherUserId));
    
    Long frId = frList.get(0).getId();
    Friendship frRequest = entityManager.find(Friendship.class, frId);

    // 1º Eliminar la request de la bbdd
    entityManager.remove(frRequest);
    loggedUser.getFriendships().remove(frRequest);
    entityManager.merge(loggedUser);
    entityManager.flush();

    return "{\"result\": \"friend request canceled.\"}";
  }  

  /**
   * Removes the friendship between current user and other user
   * @param otherUserId the user to unfriend
   * @param model
   * @param session current session used for this request
   * @return request result status
   */
  @PostMapping("/finish/{otherUserId}")
  @ResponseBody
  @Transactional
  public String finishFriendship(@PathVariable long otherUserId, Model model, HttpSession session) {
    User loggedUser = entityManager.find(User.class, ((User)session.getAttribute("u")).getId());
    User otherUser = entityManager.find(User.class, otherUserId);

    List<Friendship> frList1 = new ArrayList<>(loggedUser.getFriendships());
    frList1.removeIf(f -> (f.getUser2().getId() != otherUser.getId()));
    Long frId1 = frList1.get(0).getId();

    List<Friendship> frList2 = new ArrayList<>(otherUser.getFriendships());
    frList2.removeIf(f -> (f.getUser2().getId() != loggedUser.getId()));
    Long frId2 = frList2.get(0).getId();

    Friendship fr1 = entityManager.find(Friendship.class, frId1);
    Friendship fr2 = entityManager.find(Friendship.class, frId2);
    entityManager.remove(fr1);
    entityManager.remove(fr2);

    // Eliminar las friendships de la bbdd
    entityManager.remove(fr1);
    entityManager.remove(fr2);
    loggedUser.getFriendships().remove(fr1);
    otherUser.getFriendships().remove(fr2);
    entityManager.merge(loggedUser);
    entityManager.merge(otherUser);
    entityManager.flush();
    
    // Registrar accion en logs    
    log.info("Removing friendship between '{}' to '{}'", loggedUser.getUsername(), otherUser.getUsername());

    return "{\"result\": \"friendship finished.\"}";
  }
}