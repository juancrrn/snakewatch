package es.ucm.fdi.iw.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("levels")
public class LevelController {
    
    @GetMapping
    public String getLevels(Model model){
        File folder = new File("./src/main/resources/static/levelMaps");
        File[] foldersList = folder.listFiles();
        List<String> filesNames = new ArrayList<String>();
        for(int i = 0; i < foldersList.length; i++){
            String f = foldersList[i].getName();
            filesNames.add(f.substring(0, f.lastIndexOf(".") ));
        }
        model.addAttribute("levelMaps", filesNames);
        return "levels";
    }

    @GetMapping("/play/{levelName}")
    public String playLevel(@PathVariable String levelName  ,Model model){
        levelName += ".json";
        model.addAttribute("levelName", levelName);
        return "game";
    }


}
