package es.ucm.fdi.iw.controller;

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
import es.ucm.fdi.iw.model.UserReport;

/**
 * Site administration
 * 
 * Authentication is required to access this endpoint.
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
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private EntityManager entityManager;

    /**
     * List all users' reports
     *
     * FIXME Remove if unused
     */
    @GetMapping("/")
    public String index(Model model) {
        List<UserReport> reports = entityManager.createNamedQuery("UserReport.getAll", UserReport.class)
                .getResultList();
        model.addAttribute("reports", reports);
        return "admin";
    }

    @PostMapping("/{id}/ignore")
    @ResponseBody
    @Transactional
    public String ignore(Model model, HttpSession session, @PathVariable long id) {
        UserReport report = entityManager.find(UserReport.class, id);
        User admin = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());

        report.setStatus(UserReport.Status.CLOSED);
        report.setModeratorUser(admin);
        entityManager.persist(report);

        return "{\"result\":\"ignored\",\"id\":\"" + id + "\"}";
    }

    @PostMapping("/{id}/ban")
    @ResponseBody
    @Transactional
    public String ban(Model model, HttpSession session, @PathVariable long id) {
        UserReport report = entityManager.find(UserReport.class, id);
        User admin = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());

        // TODO: ban user

        report.setStatus(UserReport.Status.CLOSED);
        report.setModeratorUser(admin);
        entityManager.persist(report);

        return "{\"result\":\"banned\",\"id\":\"" + id + "\"}";
    }
}
