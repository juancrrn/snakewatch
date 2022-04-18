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
    public String rankings(Model model) {

        LocalDate nowDate = LocalDate.now();

        LocalDate initialMonthDate = LocalDate.of(nowDate.getYear(), nowDate.getMonthValue(), 1);

        LocalDate initialWeekDate = nowDate.with(WeekFields.of(Locale.FRANCE).dayOfWeek(),1);
        
        List<Object[]> globalRanking = entityManager
            .createNamedQuery("MatchPlayer.globalRanking", Object[].class)
            .getResultList();
        
        // FIXME Change named query to English
        List<Object[]> monthRanking = entityManager
        .createNamedQuery("MatchPlayer.rankingEntreFechas", Object[].class)
        .setParameter("fechaInicial", initialMonthDate)
        .setParameter("fechaFinal", nowDate)
        .getResultList();
        
        List<Object[]> weekRanking = entityManager
        .createNamedQuery("MatchPlayer.rankingEntreFechas", Object[].class)
        .setParameter("fechaInicial", initialWeekDate)
        .setParameter("fechaFinal", nowDate)
        .getResultList();
        
        model.addAttribute("weekRanking", weekRanking);

        model.addAttribute("monthRanking", monthRanking);

        model.addAttribute("globalRanking", globalRanking);

        return "rankings";
    }
}
