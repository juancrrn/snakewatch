package es.ucm.fdi.iw.controller;

import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("levels")
public class LevelController {
    
@GetMapping
    public String getLevels(Model model){
        File carpeta = new File("./resources/static/levelMaps");
        String[] listado = carpeta.list();
        /*if (listado == null || listado.length == 0) {
        }
        else {
            for (int i=0; i< listado.length; i++) {
                System.out.println(listado[i]);
            }
        }*/
        
        return "levels";
    }

}
