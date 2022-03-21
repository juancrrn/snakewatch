package es.ucm.fdi.iw.controller;


import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;

import java.util.List;


import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Ranking controller
 * 
 * @author Daniel Marín Irún
 * @author Juan Carrión Molina
 * @author Mohamed Ghanem
 * @author Óscar Caro Navarro
 * @author Óscar Molano Buitrago
 * 
 * @version 0.0.1
 */
@Controller
@RequestMapping("ranking")
public class RankingController {

    @Autowired
    private EntityManager entityManager;

    /**
     * Rankings view
     */
    @GetMapping("/rankings")
    public String rankings(Model model){

        
        LocalDate nowDate = LocalDate.now();

        int mes = nowDate.getMonthValue();

        WeekFields w = WeekFields.ISO;

        int semana = nowDate.get(w.weekOfWeekBasedYear());


        List<Object[]> ranking_semanal = new ArrayList<Object[]>();

        List<Object[]> ranking_mensual = new ArrayList<Object[]>();
        
        List<Object[]> ranking_global = entityManager
            .createNamedQuery("MatchPlayer.ranking", Object[].class)
            .getResultList();
        
        LocalDate listDate;

        for(int i=0;i<ranking_global.size();i++){      
            listDate = (LocalDate) ranking_global.get(i)[2];
            if(listDate.getMonthValue()==mes){
                ranking_mensual.add(ranking_global.get(i));
            }
            if(listDate.get(w.weekOfWeekBasedYear())==semana){
                ranking_semanal.add(ranking_global.get(i));
            }
        }
        

        model.addAttribute("ranking_semanal", ranking_semanal);

        model.addAttribute("ranking_mensual", ranking_mensual);

        model.addAttribute("ranking_global", ranking_global);

        return "rankings";
    }
}
