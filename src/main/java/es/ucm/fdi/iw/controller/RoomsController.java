package es.ucm.fdi.iw.controller;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.Room;

@Controller
@RequestMapping("rooms")
public class RoomsController {
    
    @Autowired
    private EntityManager entityManager;

    @GetMapping
    public String getRooms(Model model){

        List<Room> rooms = entityManager
            .createNamedQuery("Room.getRooms", Room.class)
            .getResultList();


        model.addAttribute("rooms", rooms);
        return "rooms";
    }


    @GetMapping("{id}")
    public String getRoom(@PathVariable long id, Model model){
        Room room = entityManager.find(Room.class, id);

        model.addAttribute("room", room);
        

        return "room";
    }
}
