package es.ucm.fdi.iw.controller;

import java.util.List;

import javax.persistence.EntityManager;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

  //private static final Logger log = LogManager.getLogger(AdminController.class);

  @GetMapping("/")
  public String index(Model model) {

    List<UserReport> reports = entityManager.createNamedQuery("UserReport.getAll", UserReport.class).getResultList();
    model.addAttribute("reports", reports);

    return "admin";
  }
}
