package es.ucm.fdi.iw.controller;


import java.time.LocalDate;
import java.time.temporal.WeekFields;


import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}
