package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	private static final Logger log = LogManager.getLogger(RootController.class);

	@GetMapping("/")
    public String lobby(Model model) {
        return "lobby";
    }

	@GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
    }

    @GetMapping("/levels")
    public String levels(Model model) {
        return "levels";
    }

    @GetMapping("/game")
    public String game(Model model){
        return "game";
    }

    @GetMapping("/rankings")
    public String rankings(Model model){
        return "rankings";
    }
}
