package es.ucm.fdi.iw.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.User;

@Controller
@RequestMapping("skins")
public class SkinsController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private HttpSession session;

    @GetMapping
    public String getSkins(Model model){
        Long sessionUserId = ((User) session.getAttribute("u")).getId();
        User sessionUser = entityManager.find(User.class, sessionUserId);

        File skinsFolder = new File("./src/main/resources/static/img/Skins");
        String[] skinsNames = skinsFolder.list();
        List<String> skinsList = new ArrayList<String>(Arrays.asList(skinsNames));
        

        model.addAttribute("skins", skinsList);

        model.addAttribute("playerSkin", sessionUser.getSkin());
        
        return "skins";
    }


    @PostMapping("/change_skin/{skin}")
    @Transactional
    @ResponseBody
    public String changeSkin(@PathVariable String skin, Model model){

        Long sessionUserId = ((User) session.getAttribute("u")).getId();
        User sessionUser = entityManager.find(User.class, sessionUserId);

        sessionUser.setSkin(skin);
        entityManager.persist(sessionUser);
        entityManager.flush();
        return "{\"result\": \"skin changed\", \"skin\":\"" + skin + "\"}";
    }
    
}
