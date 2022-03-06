package es.ucm.fdi.iw.model;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class MatchPlayerKey implements Serializable {

    @ManyToOne
    @JoinColumn(name="match_id")
    private Match match;

    @ManyToOne
    @JoinColumn(name="player_id")
    private User player;

}
