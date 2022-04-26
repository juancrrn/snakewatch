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
 * For each friendship between users, there are 2 instances of the database:
 * (u1, u2) y (u2, u1)
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
@NamedQuery(
    name = "Friendship.getRequests",
    query = "SELECT frq FROM Friendship frq "
          + "WHERE frq.user2.id =: userId AND frq.status = 0"
)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "user1_id", "user2_id" }))
public class Friendship implements Serializable {

    /**
     * Identifier
     * 
     * This @SequenceGenerator creates a sequence generator named
     * "friendship_id_seq_gen" based on a sequence "friendship_id_seq" autocreated
     * previously by the persistence provider, H2. This sequence will be used
     * later to fill the "User.id" field.
     * 
     * Setting "allocationSize" to 1 allows the allocated sequence space to be
     * just one, avoiding id gaps.
     */
    @Id
    @SequenceGenerator(name = "friendship_id_seq_gen", sequenceName = "friendship_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friendship_id_seq_gen")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

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

    
    public Friendship(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = Status.ACCEPTED;
        // TODO: should start as PENDING, and then be updated to ACCEPTED
        // TODO: when creating a new Friendship, we should add 2 instances on the
        // database: (u1, u2) y (u2,u1)
    }
    
}