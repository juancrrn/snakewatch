package es.ucm.fdi.iw.model;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class MatchPlayerKey implements Serializable {

    @Column(name = "match_id")
    private Long matchId;

    @Column(name = "player_id")
    private Long playerId;

}
