package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "player_id", "level_id" }))

@NamedQuery(
    name = "UserLevel.getUserLevel",
    query = "SELECT l FROM UserLevel l WHERE l.player.id =: userId AND l.level.id =: levelId"
)
@NamedQuery(
    name = "UserLevel.getUserLevelHighscores",
    query = "SELECT l FROM UserLevel l WHERE l.player.id =: userId"
)
public class UserLevel implements Serializable {

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
    @SequenceGenerator(name = "userlevel_id_seq_gen", sequenceName = "userlevel_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userlevel_id_seq_gen")
    private long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private User player;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    private int highscore;

    public UserLevel(User player, Level level, int highscore){
        this.player = player;
        this.level = level;
        this.highscore = highscore;
    }
}
