package es.ucm.fdi.iw.controller;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

        Long userId = ((User)session.getAttribute("u")).getId();
        List<Room> rooms = entityManager
            .createNamedQuery("Room.getRooms", Room.class)
            .getResultList();

        List<Boolean> isUserInRooms = new ArrayList<>();
        for (Room room : rooms){
            Boolean aux = false;
            for (RoomUser ru : room.getRoomUsers()){
                if (ru.getUser().getId() == userId){
                    aux = true;
                }
            }
            isUserInRooms.add(aux);
        }

        List<List<Object>> roomsInfo = new ArrayList<>();

        for(int i=0; i<rooms.size();i++){
            List<Object> o = new ArrayList<>();
            o.add(rooms.get(i));
            o.add(isUserInRooms.get(i));
            roomsInfo.add(o);
        }

        model.addAttribute("roomsInfo", roomsInfo);
        return "rooms";
    }

    @GetMapping("join/{roomId}")
    @MessageMapping
    @Transactional
    public String joinRoom(@PathVariable long roomId, Model model)
    throws JsonProcessingException {
        Room room = entityManager.find(Room.class, roomId);
        Long joinUserId = ((User)session.getAttribute("u")).getId();
		User joinUser = entityManager.find(User.class, joinUserId);
       
        RoomUser ru = new RoomUser();
        ru.setAdmin(false);
        ru.setRoom(room);
        ru.setUser(joinUser);

        entityManager.persist(ru);
        entityManager.flush();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("from", "rooms");
        rootNode.put("to", "room" + roomId);
        rootNode.put("type", "join");
        rootNode.put("playersOnline", room.getRoomUsers().size());
        rootNode.put("userJoiner", joinUser.getUsername());
        String json = mapper.writeValueAsString(rootNode);

        messagingTemplate.convertAndSend("/topic/room" + roomId, json);

        return getRoom(roomId, model);
    }


    @GetMapping("{roomId}")
    public String getRoom(@PathVariable long roomId, Model model)
    throws JsonProcessingException {
        Room room = entityManager.find(Room.class, roomId);
        model.addAttribute("room", room);
        
        List<Match> matches = room.getMatches();
        // Ordenar los players de cada match segun su posicion de resultado
        for(int i=0; i < matches.size();i++){
            matches.get(i).getMatchPlayers().sort(Comparator.comparing(MatchPlayer::getPosition));
        }
        model.addAttribute("matches", matches);

        return "room";
    }

    @PostMapping("/leave_room/{roomId}")
    @ResponseBody
    @Transactional
    public String leaveRoom(@PathVariable long roomId, Model model)
    throws JsonProcessingException {
        User leaveUser =  (User) session.getAttribute("u");

        RoomUser ru = entityManager
            .createNamedQuery("RoomUser.getRoomUser", RoomUser.class)
            .setParameter("roomId", roomId)
            .setParameter("userId", leaveUser.getId())
            .getSingleResult();

        leaveUser.getRoomUsers().remove(ru);
        
        entityManager.remove(ru);
        entityManager.flush();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("from", "rooms");
        rootNode.put("to", "room" + roomId);
        rootNode.put("type", "leave");
        rootNode.put("userLeaver", ru.getUser().getUsername());
        String json = mapper.writeValueAsString(rootNode);

        messagingTemplate.convertAndSend("/topic/room" + roomId, json);

        return "{\"result\": \"leave room complete.\"}";
    }
    
}
