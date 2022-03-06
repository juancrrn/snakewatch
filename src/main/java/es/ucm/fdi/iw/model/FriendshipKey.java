package es.ucm.fdi.iw.model;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class FriendshipKey implements Serializable {

    @ManyToOne
    @JoinColumn(name="user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name="user2_id")
    private User user2;

}
