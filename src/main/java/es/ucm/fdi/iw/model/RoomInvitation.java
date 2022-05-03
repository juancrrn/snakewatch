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
    name = "RoomInvitation.getReceiverInvitations",
    query = "SELECT ri FROM RoomInvitation ri "
          + "WHERE ri.receiver.id = :receiverId"
)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "room_id", "sender_id", "receiver_id" }))
public class RoomInvitation implements Serializable {

    /**
     * Identifier
     * 
     * This @SequenceGenerator creates a sequence generator named
     * "roomInvitation_id_seq_gen" based on a sequence "roomInvitation_id_seq" autocreated
     * previously by the persistence provider, H2. This sequence will be used
     * later to fill the "User.id" field.
     * 
     * Setting "allocationSize" to 1 allows the allocated sequence space to be
     * just one, avoiding id gaps.
     */
    @Id
    @SequenceGenerator(name = "roominvitation_id_seq_gen", sequenceName = "roominvitation_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roominvitation_id_seq_gen")
    private long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

}