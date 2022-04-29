package es.ucm.fdi.iw.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.Match;
import es.ucm.fdi.iw.model.MatchPlayer;
import es.ucm.fdi.iw.model.Match.Status;
import es.ucm.fdi.iw.model.User;

@Controller
@RequestMapping("spectate")
public class SpectateController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private HttpSession session;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @GetMapping
    public String spectate(Model model){ 
        return "spectate";
    }

    @GetMapping("get_ongoing_matches")
    @ResponseBody
    public String getOngoingMatches() throws JsonProcessingException{

        Long sessionUserId = ((User) session.getAttribute("u")).getId();
        User u = entityManager.find(User.class, sessionUserId);

        List<Match> onGoingMatches = entityManager
                .createNamedQuery("Match.getOngoingMatches", Match.class)
                .setParameter("status", Status.ONGOING)
                .getResultList();

        for(MatchPlayer mp: u.getMatchPlayers()){
            if(onGoingMatches.indexOf(mp.getMatch())!=-1){
                onGoingMatches.remove(mp.getMatch());
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        ArrayNode arrayMatches = rootNode.putArray("arrayMatches");
        for(Match m : onGoingMatches){
            List<Object> o = new ArrayList<>();
            o.add(m.getId());
            o.add(m.getRoom().getId());
            JsonNode j = mapper.convertValue(o, JsonNode.class);
            arrayMatches.add(j);
        }
        String json = mapper.writeValueAsString(rootNode);
        return json;
    }

}
