package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("match")
public class MatchController {

    @Autowired
    private EntityManager entityManager;

    @GetMapping("/play")
    public String playMatch(Model model){

        return "match";
    }

    @GetMapping("/play/{level_id}")
    public String playMatch(@PathVariable long level_id, Model model){
        model.addAttribute("level_id", level_id);
        return "match";
    }
}
