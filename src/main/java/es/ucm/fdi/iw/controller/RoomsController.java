package es.ucm.fdi.iw.controller;

import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.Match;
import es.ucm.fdi.iw.model.MatchPlayer;
import es.ucm.fdi.iw.model.Room;
import es.ucm.fdi.iw.model.RoomUser;
import es.ucm.fdi.iw.model.User;

@Controller
@RequestMapping("rooms")
public class RoomsController {
    
    private static final Logger log = LogManager.getLogger(RoomsController.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired 
    private HttpSession session;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @GetMapping
    public String getRooms(Model model){

        List<Room> rooms = entityManager
            .createNamedQuery("Room.getRooms", Room.class)
            .getResultList();


        model.addAttribute("rooms", rooms);
        return "rooms";
    }


    @GetMapping("{roomId}")
    @MessageMapping
    @Transactional
    public String getRoom(@PathVariable long roomId, Model model)
    throws JsonProcessingException {
        Room room = entityManager.find(Room.class, roomId);

        model.addAttribute("room", room);
        
        List<Match> matches = entityManager
            .createNamedQuery("Match.getRoomsMatches", Match.class)
            .setParameter("roomId", room.getId())
            .getResultList();

        for(int i=0; i < matches.size();i++){
            matches.get(i).getMatchPlayers().sort(Comparator.comparing(MatchPlayer::getPosition));
        }
        model.addAttribute("matches", matches);

        User joinUser =  (User) session.getAttribute("u");

        RoomUser ru = new RoomUser();

        ru.setAdmin(false);
        ru.setRoom(entityManager.find(Room.class, roomId));
        ru.setUser(entityManager.find(User.class, joinUser.getId()));

        entityManager.persist(ru);
        entityManager.flush();


        ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("from", "rooms");
		rootNode.put("to", "room" + roomId);
		rootNode.put("playersOnline", room.getRoomUsers().size());
        rootNode.put("userJoiner", joinUser.getUsername());
		String json = mapper.writeValueAsString(rootNode);

        messagingTemplate.convertAndSend("/topic/room" + roomId, json);

        return "room";
    }

    @PostMapping("/leave_room/{roomId}/{userleaveId}")
    @ResponseBody
    @Transactional
    public String leaveRoom(@PathVariable long roomId, @PathVariable long userleaveId, Model model){
        RoomUser ru = entityManager
            .createNamedQuery("RoomUser.getRoomUser", RoomUser.class)
            .setParameter("roomId", roomId)
            .setParameter("userId", userleaveId)
            .getSingleResult();

        
        entityManager.remove(ru);
        entityManager.flush();

        return "{\"result\": \"leave room complete.\"}";
    }
    
}
