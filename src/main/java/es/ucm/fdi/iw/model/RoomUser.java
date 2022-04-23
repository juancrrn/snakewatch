package es.ucm.fdi.iw.model;

import javax.persistence.*;

import org.hibernate.annotations.NamedQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Room user model
 * 
 * Many users to a single room relationship, users that are in a room.
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
/*
FIXME If this code is unuseful, remove it.
@NamedQuery(
	name = "RoomUser.getRoomUsers",
	query = "SELECT ru FROM RoomUser ru"
)
*/
@NamedQuery(
	name = "RoomUser.getRoomUser",
	query = "SELECT ru FROM RoomUser ru WHERE ru.user.id= :userId AND ru.room.id= :roomId"
)
@NamedQuery(
	name = "RoomUser.getUsersOfRoom",
	query = "SELECT ru FROM RoomUser ru WHERE ru.room.id= :roomId"
)
@NamedQuery(
	name = "RoomUser.getUserInRooms",
	query = "SELECT ru FROM RoomUser ru WHERE ru.user.id= :userId"
)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "room_id" }))
public class RoomUser{

    /**
	 * Identifier
	 * 
	 * This @SequenceGenerator creates a sequence generator named
	 * "roomuser_id_seq_gen" based on a sequence "roomuser_id_seq" autocreated
	 * previously by the persistence provider, H2. This sequence will be used
	 * later to fill the "User.id" field.
	 * 
	 * Setting "allocationSize" to 1 allows the allocated sequence space to be
	 * just one, avoiding id gaps.
	 */
    @Id
	@SequenceGenerator(name = "roomuser_id_seq_gen", sequenceName = "roomuser_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomuser_id_seq_gen")
	private long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; 

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room; 

    /** User is admin of the room */
    private boolean admin;

}
