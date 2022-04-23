package es.ucm.fdi.iw.controller;

import java.util.Comparator;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Match;
import es.ucm.fdi.iw.model.MatchPlayer;
import es.ucm.fdi.iw.model.Room;
import es.ucm.fdi.iw.model.RoomUser;
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
            for (RoomUser ru : room.getRoomUsers()) {
                if (ru.getUser().getId() == userId) {
                    aux = true;
                }
            }
            isUserInRooms.add(aux);
        }

        List<List<Object>> roomsInfo = new ArrayList<>();

        for (int i = 0; i < rooms.size(); i++) {
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
        Long joinUserId = ((User) session.getAttribute("u")).getId();
        User joinUser = entityManager.find(User.class, joinUserId);

        RoomUser ru = new RoomUser();
        ru.setAdmin(false);
        ru.setRoom(room);
        ru.setUser(joinUser);

        entityManager.persist(ru);
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

        Room room = entityManager.find(Room.class, roomId);
        model.addAttribute("room", room);

        Long sessionUserId = ((User) session.getAttribute("u")).getId();

        boolean isAdmin = false;

        List<Match> matches = room.getMatches();
        // Ordenar los players de cada match segun su posicion de resultado
        for (int i = 0; i < matches.size(); i++) {
            matches.get(i).getMatchPlayers().sort(Comparator.comparing(MatchPlayer::getPosition));
        }

        model.addAttribute("matches", matches);

        for (int i = 0; i < room.getRoomUsers().size(); i++) {
            if (room.getRoomUsers().get(i).isAdmin() && room.getRoomUsers().get(i).getUser().getId() == sessionUserId) {
                isAdmin = true;
                break;
            }
        }

        model.addAttribute("admin", isAdmin);
        return "room";
    }

    @PostMapping("create_room")
    @Transactional
    public String createRoom(Model model) {

        Room room = new Room();
        room.setVisibility(RoomType.PUBLIC);
        room.setMaxUsers(5);

        Long adminId = ((User) session.getAttribute("u")).getId();
        User adminUser = entityManager.find(User.class, adminId);

        RoomUser ruAdmin = new RoomUser();

        ruAdmin.setAdmin(true);
        ruAdmin.setRoom(room);
        ruAdmin.setUser(adminUser);

        entityManager.persist(room);
        entityManager.persist(ruAdmin);
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

        RoomUser ru = entityManager
                .createNamedQuery("RoomUser.getRoomUser", RoomUser.class)
                .setParameter("roomId", roomId)
                .setParameter("userId", leaveUser.getId())
                .getSingleResult();

        entityManager.remove(ru);
        entityManager.flush();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("type", "leaveRoom");
        rootNode.put("message", ru.getUser().getUsername());
        String json = mapper.writeValueAsString(rootNode);

        messagingTemplate.convertAndSend("/topic/room" + roomId, json);

        return "redirect:/rooms";
    }

    @PostMapping("/delete_room/{roomId}")
    @Transactional
    public String deleteRoom(@PathVariable long roomId, Model model) {

        Room r = entityManager.find(Room.class, roomId);

        List<RoomUser> roomUsers = entityManager
                .createNamedQuery("RoomUser.getUsersOfRoom", RoomUser.class)
                .setParameter("roomId", roomId)
                .getResultList();

        for (int i = 0; i < roomUsers.size(); i++) {
            entityManager.remove(roomUsers.get(i));
        }
        entityManager.remove(r);
        entityManager.flush();
        return "redirect:/rooms";
    }

    @GetMapping("/go_to_match/{matchId}/{roomId}")
    public String goToMatch(@PathVariable long matchId, @PathVariable long roomId, Model model) {

        Match match = entityManager.find(Match.class, matchId);
        Room room = entityManager.find(Room.class, roomId);

        model.addAttribute("room", room);
        model.addAttribute("match", match);
        model.addAttribute("admin", false);

        return getMatch(roomId, model);
    }

    @GetMapping("/get_match/{roomId}")
    @Transactional
    public String getMatch(@PathVariable long roomId, Model model) {

        Room room = entityManager.find(Room.class, roomId);

        Long sessionUserId = ((User) session.getAttribute("u")).getId();

        for (int i = 0; i < room.getRoomUsers().size(); i++) {
            if (room.getRoomUsers().get(i).isAdmin() && room.getRoomUsers().get(i).getUser().getId() == sessionUserId) {
                Match match = new Match();

                match.setRoom(room);
                match.setDate(LocalDate.now());
                match.setStatus(Status.WAITING);

                model.addAttribute("room", room);
                model.addAttribute("match", match);
                model.addAttribute("admin", true);

                entityManager.persist(match);
                entityManager.flush();
            }
        }

        return "game";
    }

}
