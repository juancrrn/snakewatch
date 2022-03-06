package es.ucm.fdi.iw.model;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class FriendshipKey implements Serializable {

    public FriendshipKey(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;
    }
    @ManyToOne
    @JoinColumn(name="user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name="user2_id")
    private User user2;

}
