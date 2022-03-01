package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Match player model
 * 
 * Many users to a single match relationship, users that are playing a match.
 * The same user cannot be playing in different matches at the same time.
 * 
 * @author Daniel Marín Irún
 * @author Juan Carrión Molina
 * @author Mohamed Ghanem
 * @author Óscar Caro Navarro
 * @author Óscar Molano Buitrago
 * 
 * @version 0.0.1
 */
@Entity
@Data
@NoArgsConstructor
public class MatchPlayer implements Serializable{


    @Id
    @ManyToOne
    @MapsId("matchId")
    private Match match;

    @Id
    @ManyToOne
    @MapsId("userId")
    private User user;

    private int result;
}