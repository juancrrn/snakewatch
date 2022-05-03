package es.ucm.fdi.iw.controller;

import java.util.Comparator;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.Friendship;
import es.ucm.fdi.iw.model.Match;
import es.ucm.fdi.iw.model.MatchPlayer;
import es.ucm.fdi.iw.model.Room;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Match.Status;
import es.ucm.fdi.iw.model.Room.RoomType;

@Controller
@RequestMapping("rooms")
public class RoomsController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private HttpSession session;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public String getRooms(Model model) {

        Long userId = ((User) session.getAttribute("u")).getId();
        List<Room> rooms = entityManager
                .createNamedQuery("Room.getAll", Room.class)
                .getResultList();

        List<Boolean> isUserInRooms = new ArrayList<>();
        for (Room room : rooms) {
            Boolean aux = false;
            for (User ru : room.getUsers()) {
                if (ru.getId() == userId) {
                    aux = true;
                }
            }
            isUserInRooms.add(aux);
        }

        List<List<Object>> roomsInfo = new ArrayList<>();

        for (int i = 0; i < rooms.size(); i++) {
            List<Object> o = new ArrayList<>();
            o.add(rooms.get(i));
            o.add(rooms.get(i).getOwner());
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
        Long joinUserId = ((User) session.getAttribute("u")).getId();
        User joinUser = entityManager.find(User.class, joinUserId);

        room.getUsers().add(joinUser);
        entityManager.persist(room);
        entityManager.flush();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("type", "joinRoom");
        rootNode.put("message", joinUser.getUsername());
        String json = mapper.writeValueAsString(rootNode);

        messagingTemplate.convertAndSend("/topic/room" + roomId, json);

        return getRoom(roomId, model);
    }

    @GetMapping("{roomId}")
    public String getRoom(@PathVariable long roomId, Model model) {
        Long sessionUserId = ((User) session.getAttribute("u")).getId();
        Room room = entityManager.find(Room.class, roomId);
        model.addAttribute("room", room);

        List<Match> matches = room.getMatches();
        // Ordenar los players de cada match segun su posicion de resultado
        for (Match match : matches) {
            match.getMatchPlayers().sort(Comparator.comparing(MatchPlayer::getPosition));
        }

        model.addAttribute("matches", matches);

        boolean isOwner = room.getOwner().getId() == sessionUserId;

        model.addAttribute("admin", isOwner);
        return "room";
    }

    @PostMapping("create_room")
    @Transactional
    public String createRoom(Model model) {

        Room room = new Room();
        room.setVisibility(RoomType.PUBLIC);
        room.setMaxUsers(5);

        // Establecer al usuario como administrador de esta room
        Long ownerId = ((User) session.getAttribute("u")).getId();
        User owner = entityManager.find(User.class, ownerId);
        room.setOwner(owner);
        room.getUsers().add(owner);

        entityManager.persist(room);
        entityManager.flush();

        return "redirect:/rooms/" + room.getId();
    }

    @PostMapping("/edit_room/{roomId}")
    @Transactional
    public String editRoom(@PathVariable long roomId, @RequestParam String roomVisibility, @RequestParam int maxPlayers,
            Model model) {
        Room room = entityManager.find(Room.class, roomId);

        room.setVisibility(RoomType.valueOf(roomVisibility));
        room.setMaxUsers(maxPlayers);
        entityManager.persist(room);
        entityManager.flush();

        return "redirect:/rooms/" + roomId;
    }

    @PostMapping("/leave_room/{roomId}")
    @Transactional
    public String leaveRoom(@PathVariable long roomId, Model model)
            throws JsonProcessingException {
        Long leaveUserId = ((User) session.getAttribute("u")).getId();
        User leaveUser = entityManager.find(User.class, leaveUserId);

        Room room = entityManager.find(Room.class, roomId);
        room.getUsers().remove(leaveUser);
        if(room.getOwner().getId() == leaveUserId){
            room.setOwner(null);
        }

        entityManager.persist(room);
        entityManager.flush();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("type", "leaveRoom");
        rootNode.put("message", leaveUser.getUsername());
        String json = mapper.writeValueAsString(rootNode);

        messagingTemplate.convertAndSend("/topic/room" + roomId, json);

        return "redirect:/rooms";
    }

    @PostMapping("/delete_room/{roomId}")
    @Transactional
    public String deleteRoom(@PathVariable long roomId, Model model) {

        Room r = entityManager.find(Room.class, roomId);
        r.getUsers().clear();
        r.setOwner(null);

        // Eliminar la room
        entityManager.remove(r);
        entityManager.flush();
        return "redirect:/rooms";
    }

    @PostMapping("invite_friends_to_room/{roomId}")
    @MessageMapping
    @ResponseBody
    public String sendInvitationsToJoinPrivateRoom(@PathVariable long roomId, Model model) 
        throws JsonProcessingException{

        Long sessionUserId = ((User) session.getAttribute("u")).getId();
        List<Friendship> friendships = entityManager
                .createNamedQuery("Friendship.getFriends", Friendship.class)
                .setParameter("userId", sessionUserId)
                .getResultList();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        
        rootNode.put("type", "roomInvitation");

        for(Friendship f: friendships){
            rootNode.put("message", roomId);
            String json = mapper.writeValueAsString(rootNode);
            messagingTemplate.convertAndSend("/user/" + f.getUser2().getUsername() + "/queue/updates", json);
        }
        return "{\"result\": \"invitations sent.\"}";
    }

    @GetMapping("/get_match/{matchId}")
    public String getMatch(@PathVariable long matchId, Model model) {

        Match match = entityManager.find(Match.class, matchId);

        Long sessionUserId = ((User) session.getAttribute("u")).getId();
        User sessionUser = entityManager.find(User.class, sessionUserId);

        Room room = match.getRoom();

        model.addAttribute("room", room);
        model.addAttribute("match", match);
        model.addAttribute("level", "demo");

        // Lista con los los "username" de los jugadores 
        List<String> players = new ArrayList<>();
        for (MatchPlayer mp : match.getMatchPlayers()) {
            players.add(mp.getPlayer().getUsername());
        }

        model.addAttribute("players", players);

        
        model.addAttribute("admin", match.getOwner().getId() == sessionUserId);        

        if(match.getStatus()!=Status.ENDED){

            return "game";

        }
        else{

            if(players.indexOf(sessionUser.getUsername())==-1){
                return "redirect:/spectate";
            }
            else{
                return "redirect:/rooms/" + match.getRoom().getId();
            }
           
        }
    }

    @PostMapping("/create_match/{roomId}")
    @ResponseBody
    @MessageMapping
    @Transactional
    public String createMatch(@PathVariable long roomId, @RequestBody JsonNode o, Model model)
            throws JsonProcessingException {

        Room room = entityManager.find(Room.class, roomId);

        // This json contains the list of players of the match 
        ArrayNode matchPlayers = (ArrayNode) o.get("message");

        Match match = new Match();

        match.setRoom(room);
        match.setDate(LocalDate.now());
        match.setStatus(Status.WAITING);

        model.addAttribute("room", room);
        model.addAttribute("match", match);

        entityManager.persist(match);
        entityManager.flush();

        for (JsonNode j : matchPlayers) {
            User u = entityManager.createNamedQuery("User.byUsername", User.class)
                    .setParameter("username", j.textValue()).getSingleResult();
            MatchPlayer matchPlayer = new MatchPlayer();
            matchPlayer.setMatch(match);
            matchPlayer.setPlayer(u);
            entityManager.persist(matchPlayer);
            entityManager.flush();
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("type", "goToMatch");
        rootNode.put("message", match.getId());
        String json = mapper.writeValueAsString(rootNode);

        messagingTemplate.convertAndSend("/topic/room" + roomId, json);

        return "{\"result\": \"match created.\"}";
    }

    @PostMapping("start_match/{matchId}")
    @ResponseBody
    @Transactional
    public String startMatch(@PathVariable long matchId, Model model) {
        Match match = entityManager.find(Match.class, matchId);
        match.setStatus(Status.ONGOING);
        entityManager.persist(match);
        entityManager.flush();
        return "{\"result\": \"match started.\"}";
    }

    @PostMapping("/finish_match/{matchId}")
    @ResponseBody
    @Transactional
    public String finishMatch(@PathVariable long matchId, @RequestBody JsonNode o, Model model) 
        throws JsonProcessingException {
        
        Match match = entityManager.find(Match.class, matchId);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        ArrayNode matchPlayers = (ArrayNode) o.get("message");
 
        ArrayNode results = rootNode.putArray("message");

        for(int i=0; i < matchPlayers.size();i++){
            JsonNode j = matchPlayers.get(i);
            MatchPlayer mp = entityManager.
                createNamedQuery("MatchPlayer.byPlayerUsername", MatchPlayer.class)
                .setParameter("matchId", matchId)
                .setParameter("playerUserName", j.get("playerName").textValue())
                .getSingleResult();
            results.add(j);
            mp.setPosition(i+1);
            entityManager.persist(mp);
            entityManager.flush();
        }

        match.setStatus(Status.ENDED);
        entityManager.persist(match);
        entityManager.flush();
        
        rootNode.put("type", "finishMatch");
        
        String json = mapper.writeValueAsString(rootNode);

        messagingTemplate.convertAndSend("/topic/match" + matchId, json);

        return "{\"result\": \"match finished.\"}";
    }

}
