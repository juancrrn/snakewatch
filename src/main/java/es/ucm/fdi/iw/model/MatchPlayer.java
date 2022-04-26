package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@NamedQuery(
    name = "MatchPlayer.byPlayerId",
    query = "SELECT mp FROM MatchPlayer mp "
          + "WHERE mp.player.id = :playerId"
)
@NamedQuery(
    name = "MatchPlayer.byPlayerUsername",
    query = "SELECT mp FROM MatchPlayer mp "
          + "WHERE mp.match.id = :matchId AND mp.player.username= :playerUserName"
)
@NamedQuery(
    name = "MatchPlayer.getWinnerById",
    query = "SELECT COUNT(mp) FROM MatchPlayer mp "
          + "WHERE mp.position=1 AND mp.player.id = :playerId"
)
@NamedQuery(
    name = "MatchPlayer.globalRanking",
    query = "SELECT u.username, COUNT(mp.player.id) as co FROM MatchPlayer mp "
          + "JOIN User u ON u.id=mp.player.id "
          + "WHERE mp.position=1 GROUP BY u.username ORDER BY co DESC"
)
@NamedQuery(
    name = "MatchPlayer.rankingBetweenDates",
    query = "SELECT u.username, COUNT(mp.player.id) as co FROM MatchPlayer mp "
          + "JOIN Match m ON mp.match.id=m.id JOIN User u ON u.id=mp.player.id "
          + "WHERE position=1 AND date>= :initialDate AND date<= :nowDate "
          + "GROUP BY u.username ORDER BY co DESC"
)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "player_id", "match_id" }))
public class MatchPlayer implements Serializable {

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
     * 
     * @see www.baeldung.com/jpa-many-to-many
     */

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private User player;

    /**
     * Match
     * 
     * @see www.baeldung.com/jpa-many-to-many
     */
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    /**
     * Position in the ranking of the match.
     * 
     * <p>
     * - Starting from 1 (for winner)
     * <p>
     * - Null if the match hasn't ended yet.
     */
    private int position;
}
