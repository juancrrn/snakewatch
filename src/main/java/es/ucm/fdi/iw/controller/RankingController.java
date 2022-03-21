package es.ucm.fdi.iw.controller;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.model.User;

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

        
        LocalDate date = LocalDate.now();

        int mes = date.getMonthValue();


        Calendar c = new GregorianCalendar();

        int semana = c.get(Calendar.WEEK_OF_YEAR);


        List<Object[]> ranking_semanal = new ArrayList<Object[]>();

        List<Object[]> ranking_mensual = new ArrayList<Object[]>();
        
        List<Object[]> ranking_global = new ArrayList<Object[]>();

        List<Object[]> results_global = entityManager
        .createNativeQuery("SELECT player_id, COUNT(player_id) FROM Match_Player WHERE position=1 GROUP BY player_id ORDER BY COUNT(player_id) DESC, player_id")
        .getResultList();
        
        for(int i=0; i<results_global.size();i++){
            BigInteger n1 = (BigInteger) results_global.get(i)[0];
            BigInteger n2 = (BigInteger) results_global.get(i)[1];
            User u = entityManager
                .createNamedQuery("User.byId", User.class) 
                .setParameter("id", n1.longValue())
                .getSingleResult();
           
            Object[] fila = {u, n2.intValue()};

            ranking_global.add(fila);
        }

        List<Object[]> results_mensual = entityManager
        .createNativeQuery("SELECT player_id, COUNT(player_id) FROM Match_Player mp JOIN Match m WHERE mp.match_id=m.id AND mp.position=1 AND MONTH(m.date)=:mes GROUP BY player_id ORDER BY COUNT(player_id) DESC, player_id")
        .setParameter("mes", mes)
        .getResultList();


        for(int i=0; i<results_mensual.size();i++){
            BigInteger n1 = (BigInteger) results_mensual.get(i)[0];
            BigInteger n2 = (BigInteger) results_mensual.get(i)[1];
            User u = entityManager
            .createNamedQuery("User.byId", User.class) 
            .setParameter("id", n1.longValue())
            .getSingleResult();
       
            Object[] fila = {u, n2.intValue()};

            ranking_mensual.add(fila);

        }


        List<Object[]> results_semanal = entityManager
        .createNativeQuery("SELECT player_id, COUNT(player_id) FROM Match_Player mp JOIN Match m WHERE mp.match_id=m.id AND mp.position=1 AND WEEK(m.date)=:semana GROUP BY player_id ORDER BY COUNT(player_id) DESC, player_id")
        .setParameter("semana", semana)
        .getResultList();


        for(int i=0; i<results_semanal.size();i++){
            BigInteger n1 = (BigInteger) results_semanal.get(i)[0];
            BigInteger n2 = (BigInteger) results_semanal.get(i)[1];
            User u = entityManager
            .createNamedQuery("User.byId", User.class) 
            .setParameter("id", n1.longValue())
            .getSingleResult();
       
            Object[] fila = {u, n2.intValue()};

            ranking_semanal.add(fila);

        }

        model.addAttribute("ranking_semanal", ranking_semanal);

        model.addAttribute("ranking_mensual", ranking_mensual);

        model.addAttribute("ranking_global", ranking_global);

        return "rankings";
    }
}
