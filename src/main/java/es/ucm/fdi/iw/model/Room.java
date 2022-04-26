package es.ucm.fdi.iw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Room model
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
@NamedQuery(name = "Room.getAll", query = "SELECT r FROM Room r")
public class Room implements Serializable {

    /**
     * Identifier
     * 
     * <p>
     * This SequenceGenerator creates a sequence generator named
     * "room_id_seq_gen" based on a sequence "room_id_seq" autocreated
     * previously by the persistence provider, H2.
     * 
     * <p>
     * This sequence will be used later to fill the "Room.id" field.
     * 
     * <p>
     * Setting "allocationSize" to 1 allows the allocated sequence space to be
     * just one, avoiding id gaps.
     */
    @Id
    @SequenceGenerator(name = "room_id_seq_gen", sequenceName = "room_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_id_seq_gen")
    private long id;

    /**
     * Visibility of the room
     * <p>
     * - PUBLIC: anyone can join
     * <p>
     * - PRIVATE: needs permission to join (passwork, link...)
     */
    public enum RoomType {
        PUBLIC,
        PRIVATE,
    }

    private RoomType visibility;

    /**
     * Max number of users in the room
     * 
     * <p>
     * If null, the match has no maximum.
     */

    @Size(min = 2, max = 6, message = "Minimum 2 players, Maximum 6 players in a Room")
    private int maxUsers;

    /**
     * List of users in this room
     * 
     * @see www.baeldung.com/jpa-many-to-many
     */
    @ManyToMany
    @JoinTable(name = "room_users", 
        joinColumns = @JoinColumn(name = "room_id"), 
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    private User owner;

    
    /**
     * List of matches played in this room
     */
    @OneToMany(mappedBy = "room")
    private List<Match> matches = new ArrayList<>();
}