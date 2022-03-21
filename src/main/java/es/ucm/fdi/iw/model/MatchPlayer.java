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
    @NamedQuery(name="MatchPlayer.getMatchPlayers",
        query="SELECT mp FROM MatchPlayer mp"),
    @NamedQuery(name="MatchPlayer.byPlayerId",
        query="SELECT mp FROM MatchPlayer mp WHERE mp.player.id = :playerId"),
    @NamedQuery(name="MatchPlayer.getWinnerById",
        query="SELECT COUNT(mp) FROM MatchPlayer mp WHERE mp.position=1 AND mp.player.id = :playerId"),
    @NamedQuery(name="MatchPlayer.ranking",
    query="SELECT u.username, COUNT(mp.player.id) as co, m.date FROM MatchPlayer mp JOIN Match m ON m.id=mp.match.id JOIN User u ON u.id=mp.player.id WHERE mp.position=1 GROUP BY u.username ORDER BY co DESC")
})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "player_id", "match_id" }))
public class MatchPlayer implements Serializable{

    /**
	 * Identifier
	 * 
	 * This @SequenceGenerator creates a sequence generator named
	 * "matchplayer_id_seq_gen" based on a sequence "matchplayer_id_seq" autocreated
	 * previously by the persistence provider, H2. This sequence will be used
	 * later to fill the "User.id" field.
	 * 
	 * Setting "allocationSize" to 1 allows the allocated sequence space to be
	 * just one, avoiding id gaps.
	 */
    @Id
	@SequenceGenerator(name = "matchplayer_id_seq_gen", sequenceName = "matchplayer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "matchplayer_id_seq_gen")
	private long id;

    /**
	 * Player
	 * @see www.baeldung.com/jpa-many-to-many
	 */
    @ManyToOne
    @JoinColumn(name = "player_id")
    private User player;

    /**
	 * Match
	 * @see www.baeldung.com/jpa-many-to-many
	 */
    @ManyToOne
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
