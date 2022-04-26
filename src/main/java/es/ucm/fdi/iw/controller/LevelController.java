package es.ucm.fdi.iw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.iw.model.Level;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.UserLevel;

@Controller

@RequestMapping("levels")
public class LevelController {
    
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private HttpSession session;

    @GetMapping
    public String getLevels(Model model){
        try{
            File folder = new File("./src/main/resources/static/levelMaps");
            File[] foldersList = folder.listFiles();
            List<String> filesNames = new ArrayList<>();
            for(int i = 0; i < foldersList.length; i++){
                String f = foldersList[i].getName();
                filesNames.add(f.substring(0, f.lastIndexOf(".") ));
            }
            model.addAttribute("levelMaps", filesNames);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
        }
       
        return "levels";
    }

    @PostMapping("score/{levelId}/{scorePlayer}")
    @ResponseBody
    @Transactional
    public String updateHighscore(@PathVariable long levelId, @PathVariable int scorePlayer ,Model model){
        
        Long userId = ((User) session.getAttribute("u")).getId();
        User user = entityManager.find(User.class, userId);
        Level level = entityManager.find(Level.class, levelId);
        System.out.println("USERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
        System.out.println(userId);
        System.out.println("USERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
        System.out.println("LEVELLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
        System.out.println(level.getId());
        System.out.println("LEVELLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
        UserLevel userLevel = null;
        try {
            userLevel = entityManager
            .createNamedQuery("UserLevel.getUserLevel", UserLevel.class)
            .setParameter("userId", userId)
            .setParameter("levelId", level.getId())
            .getSingleResult(); 
            System.out.println("FUCKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
            
            
        } catch (NoResultException e) {
            //TODO: handle exception
        }
        if(userLevel == null){
            userLevel = new UserLevel(user, level, scorePlayer);
        }
        else{
            if(userLevel.getHighscore() < scorePlayer){
                userLevel.setHighscore(scorePlayer);
            }
        }
        entityManager.persist(userLevel);
        entityManager.flush();
        
        
        return "{\"score\": \"yes.\"}";
    }

    

    @GetMapping("/play/{levelName}")
    public String playLevel(@PathVariable String levelName  ,Model model){

        File folder = new File("./src/main/resources/static/levelMaps");
        String[] foldersList = folder.list();
        List<String> filesNames = new ArrayList<String>(Arrays.asList(foldersList));
        model.addAttribute("nBots", filesNames.indexOf(levelName+".json") +1);
        model.addAttribute("levelName", levelName);
        return "gameLevel";
    }

    @PostMapping("load_level")
    public String loadLevel(@RequestParam("file") MultipartFile file , Model model, RedirectAttributes attributes) 
        throws IOException {

        File folder = new File("./src/main/resources/static/levelMaps");
        String[] foldersList = folder.list();
        List<String> filesNames = new ArrayList<String>(Arrays.asList(foldersList));

        if(file.isEmpty() || file==null){
            attributes.addFlashAttribute("message", "You must choose a file!!!!");
            return "redirect:/levels";
        }

        else if(!file.getOriginalFilename().endsWith(".json")){
            attributes.addFlashAttribute("message", "File must be json type!!!!");
            return "redirect:/levels";
        }
        else if(filesNames.indexOf(file.getOriginalFilename())!=-1){
            attributes.addFlashAttribute("message", "A file with the same name already exists!!!!");
            return "redirect:/levels";
        }

        byte[] bytesFile = file.getBytes();

        Path path = Paths.get("./src/main/resources/static/levelMaps//" + file.getOriginalFilename());

        Files.write(path, bytesFile);
       
        return "redirect:/levels";
    }

}
