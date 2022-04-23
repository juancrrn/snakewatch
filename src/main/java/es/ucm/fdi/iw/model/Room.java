package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
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
@NamedQueries({
    @NamedQuery(name="Room.getRooms",
        query="SELECT r FROM Room r")
})
public class Room {	
	
	/**
	 * Identifier
	 * 
	 * <p> This SequenceGenerator creates a sequence generator named
	 * "room_id_seq_gen" based on a sequence "room_id_seq" autocreated
	 * previously by the persistence provider, H2. 
     * 
     * <p> This sequence will be used later to fill the "Room.id" field.
	 * 
	 * <p> Setting "allocationSize" to 1 allows the allocated sequence space to be
	 * just one, avoiding id gaps.
	 */
    @Id
	@SequenceGenerator(name = "room_id_seq_gen", sequenceName = "room_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_id_seq_gen")
    private long id;

    /**
     * Visibility of the room
     * <p> - PUBLIC: anyone can join
     * <p> - PRIVATE: needs permission to join (passwork, link...)
     */
    public enum RoomType {
        PUBLIC,
        PRIVATE,
    }
    private RoomType visibility;

    /**
     * Max number of users in the room
     * 
     * <p> If null, the match has no maximum.
     * <p> TODO: check this constraint when adding new users to the room
     */

    @Size(min=2, max=6, message="Minimum 2 players, Maximum 6 players in a Room")
    private int maxUsers;  
    
    /**
	 * List of users in this room (as RoomUser objects)
	 * @see www.baeldung.com/jpa-many-to-many
	 */
	@OneToMany(mappedBy = "room")
	private List<RoomUser> roomUsers = new ArrayList<>();




    @OneToMany(mappedBy = "room")
    private List<Match> matches = new ArrayList<>();
/*
    @Getter
    @AllArgsConstructor
    private static class Transfer {
        private long id;
        private String roomname;
    }
    
    @Override
    public Transfer toTransfer() {
		return new Transfer(id,	roomname, received.size(), sent.size());
    }

	@Override
	public String toString() {
		return toTransfer().toString();
	}
*/
}