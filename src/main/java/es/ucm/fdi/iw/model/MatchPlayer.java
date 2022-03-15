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
@NamedQueries({
    @NamedQuery(name="MatchPlayer.byPlayerId",
        query="SELECT mp FROM MatchPlayer mp WHERE mp.id.playerId = :playerId")
})
public class MatchPlayer implements Serializable{

    @EmbeddedId
    @Column(nullable=false)
    private MatchPlayerKey id;

    /**
	 * Player
	 * @see www.baeldung.com/jpa-many-to-many
	 */
    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "player_id")
    private User player;

    /**
	 * Match
	 * @see www.baeldung.com/jpa-many-to-many
	 */
    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id")
    private Match match;

    /**
     * Position in the ranking of the match.
     * 
     * <p> - Starting from 1 (for winner)
     * <p> - Null if the match hasn't ended yet.
     */
    private int position;
}