package es.ucm.fdi.iw.controller;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Friendship;
import es.ucm.fdi.iw.model.MatchPlayer;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.RoomUser;
//import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
//import java.util.List;
import java.util.Objects;
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
     * Returns the state of friendship between user1 and user2
	 * @param sender_id user that sent the friend request
	 * @param receiver_id user that received the friend request
     */
    @PostMapping("/state/{user1_id}/{user2_id}")
	@ResponseBody
	@Transactional
	public String getFriendStatus(@PathVariable long user1_id, @PathVariable long user2_id, Model model, HttpSession session){
		
		User user1 = entityManager.find(User.class, user1_id);
		User user2 = entityManager.find(User.class, user2_id);
		
		boolean isFriends = false;
		boolean requestSent = false;
		boolean requestReceived = false;

		List<Friendship> acceptedFr = new ArrayList<>(user1.getFriendships());
		acceptedFr.removeIf(f -> (f.getStatus() != Friendship.Status.ACCEPTED || f.getUser2().getId() != user2.getId()));
		if(!acceptedFr.isEmpty()){
			isFriends = true;
		}

		List<Friendship> pendingFr = new ArrayList<>(user1.getFriendships());
		pendingFr.removeIf(f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != user2.getId()));
		if(!pendingFr.isEmpty()){
			requestSent = true;
		}

		List<Friendship> pendingReceivedFr = new ArrayList<>(user2.getFriendships());
		pendingReceivedFr.removeIf(f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != user1.getId()));
		if(!pendingReceivedFr.isEmpty()){
			requestReceived = true;
		}

		JSONObject json = new JSONObject();

		if(isFriends){
			json.put("result", "friends");
		}
		else if(requestSent){
			json.put("result", "request sent");
		}
		else if(requestReceived){
			json.put("result", "request received");
		}
		else{
			json.put("result", "not friends");
		} 

		return json.toString();
	}	


	/**
     * Sends a friend request to an user.
     * @param id of target user (source user is obtained from session attributes)
     */
    @PostMapping("/send_req/{sender_id}/{receiver_id}")
	@ResponseBody
	@Transactional
	public String sendFriendReq(@PathVariable long sender_id, @PathVariable long receiver_id, Model model, HttpSession session){
		
		User sender = entityManager.find(User.class, sender_id);
		User receiver = entityManager.find(User.class, receiver_id);

		// Contruye objeto Friendship y lo guarda en la BD
		// El estado será PENDING, hasta que el otro usuario la acepte/rechaze
		Friendship fr = new Friendship();
		fr.setUser1(sender);
		fr.setUser2(receiver);
		fr.setStatus(Friendship.Status.PENDING);
		entityManager.persist(fr);
		entityManager.flush();
		
		// Registrar accion en logs		
		log.info("Sending a friend request from '{}' to '{}'", sender.getUsername(), receiver.getUsername());

		return "{\"result\": \"ok\"}";
	}	

	/**
     * Accept a received friend request
	 * @param sender_id user that sent the friend request
	 * @param receiver_id user that received the friend request
     */
    @PostMapping("/accept_req/{sender_id}/{receiver_id}")
	@ResponseBody
	@Transactional
	public String acceptFriendReq(@PathVariable long sender_id, @PathVariable long receiver_id, Model model, HttpSession session){
		User sender = entityManager.find(User.class, sender_id);
		User receiver = entityManager.find(User.class, receiver_id);

		List<Friendship> frList = new ArrayList<>(sender.getFriendships());
		frList.removeIf(f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != receiver_id));
		
		Long frId = frList.get(0).getId();
		Friendship frRequest = entityManager.find(Friendship.class, frId);

		// 1º Actualizar la friendship request a ACCEPTED
		frRequest.setStatus(Friendship.Status.ACCEPTED);
		entityManager.merge(frRequest);

		// 2º Crear la instancia reversa de friendship
		Friendship fr2 = new Friendship();
		fr2.setUser1(receiver);
		fr2.setUser2(sender);
		fr2.setStatus(Friendship.Status.ACCEPTED);
		entityManager.persist(fr2);
		receiver.getFriendships().add(fr2);
		entityManager.merge(sender);
		entityManager.merge(receiver);
		
		entityManager.flush();
		
		// Registrar accion en logs		
		log.info("Accepting the friend request from '{}' to '{}'", sender.getUsername(), receiver.getUsername());

		return "{\"result\": \"friend request sent.\"}";
	}	

	@PostMapping("/reject_req/{sender_id}/{receiver_id}")
	@ResponseBody
	@Transactional
	public String rejectFriendReq(@PathVariable long sender_id, @PathVariable long receiver_id, Model model, HttpSession session){
		User sender = entityManager.find(User.class, sender_id);

		List<Friendship> frList = new ArrayList<>(sender.getFriendships());
		frList.removeIf(f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != receiver_id));
		
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
     * Cancel the friend request sent
	 * @param sender_id user that sent the friend request
	 * @param receiver_id user that received the friend request
     */
    @PostMapping("/cancel_req/{sender_id}/{receiver_id}")
	@ResponseBody
	@Transactional
	public String cancelFriendReq(@PathVariable long sender_id, @PathVariable long receiver_id, Model model, HttpSession session){
		User sender = entityManager.find(User.class, sender_id);

		List<Friendship> frList = new ArrayList<>(sender.getFriendships());
		frList.removeIf(f -> (f.getStatus() != Friendship.Status.PENDING || f.getUser2().getId() != receiver_id));
		
		Long frId = frList.get(0).getId();
		Friendship frRequest = entityManager.find(Friendship.class, frId);

		// 1º Eliminar la request de la bbdd
		entityManager.remove(frRequest);
		sender.getFriendships().remove(frRequest);
		entityManager.merge(sender);
		entityManager.flush();

		return "{\"result\": \"friend request canceled.\"}";
	}	

	/**
     * Finish the friendship between user1 and user2
	 * @param user1_id user that sent the friend request
	 * @param user2_id user that received the friend request
     */
    @PostMapping("/finish/{user1_id}/{user2_id}")
	@ResponseBody
	@Transactional
	public String finishFriendship(@PathVariable long user1_id, @PathVariable long user2_id, Model model, HttpSession session){
		
		User user1 = entityManager.find(User.class, user1_id);
		User user2 = entityManager.find(User.class, user2_id);

		List<Friendship> frList1 = new ArrayList<>(user1.getFriendships());
		frList1.removeIf(f -> (f.getUser2().getId() != user2.getId()));
		Long frId1 = frList1.get(0).getId();

		List<Friendship> frList2 = new ArrayList<>(user2.getFriendships());
		frList2.removeIf(f -> (f.getUser2().getId() != user1.getId()));
		Long frId2 = frList2.get(0).getId();

		Friendship fr1 = entityManager.find(Friendship.class, frId1);
		Friendship fr2 = entityManager.find(Friendship.class, frId2);
		entityManager.remove(fr1);
		entityManager.remove(fr2);

		// Eliminar las friendships de la bbdd
		entityManager.remove(fr1);
		entityManager.remove(fr2);
		user1.getFriendships().remove(fr1);
		user2.getFriendships().remove(fr2);
		entityManager.merge(user1);
		entityManager.merge(user2);
		entityManager.flush();
		
		// Registrar accion en logs		
		log.info("Removing friendship between '{}' to '{}'", user1.getUsername(), user2.getUsername());

		return "{\"result\": \"friendship finished.\"}";
	}
}