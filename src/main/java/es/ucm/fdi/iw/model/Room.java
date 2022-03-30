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
// ToDo: implements Transferable<Room.Transfer>
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
    private int maxUsers;  
    
    /**
	 * List of users in this room (as RoomUser objects)
	 * @see www.baeldung.com/jpa-many-to-many
	 */
	@OneToMany( mappedBy = "room")
	private List<RoomUser> roomUsers = new ArrayList<>();

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