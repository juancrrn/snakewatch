package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Friendship model
 * 
 * Many users to many users relationship.
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
    @NamedQuery(name="Friendship.getFriends",
            query="SELECT f FROM Friendship f "
                    + "WHERE (f.id.user1.id = :userid OR f.id.user2.id = :userid)"
                    + "AND (f.status = 1)")
})
public class Friendship implements Serializable{

    // TODO: to have a compound primary key, we need to create a separate class FriendshipId
    // Explained here: https://stackoverflow.com/questions/13032948/how-to-create-and-handle-composite-primary-key-in-jpa
    // And here: https://stackoverflow.com/questions/31385658/jpa-how-to-make-composite-foreign-key-part-of-composite-primary-key

    @EmbeddedId
    @Column(nullable=false)
    private FriendshipKey id;

    public Friendship(User user1, User user2){
        this.id.setUser1(user1);
        this.id.setUser2(user2);
        /*provisional initation*/
        this.status = Status.ACCEPTED;
        /*REAL INIITATION
        this.status = Status.PENDING;
        */
        
    }
    
    /*@ManyToOne
    @MapsId("userId")
    private User user1;

    @ManyToOne
    @MapsId("userId")
    private User user2;*/

    /**
     * Status of the friendship request
     * 
     * - Pending: request sent but not yet accepted
     * - Accepted: friendship stablished
     */
    public enum Status {
        PENDING,
        ACCEPTED
    }

    @Column(nullable = false)
    private Status status;
}