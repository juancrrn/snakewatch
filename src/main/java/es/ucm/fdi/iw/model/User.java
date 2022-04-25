package es.ucm.fdi.iw.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An authorized user of the system.
 */
@Entity
@Data
@NoArgsConstructor
/*
FIXME If this code is unuseful, remove it.
@NamedQuery(
	name = "User.getUsers",
	query = "SELECT u FROM User u")
*/
@NamedQuery(
	name = "User.byId",
	query = "SELECT u FROM User u "
		  + "WHERE u.id = :id AND u.enabled = TRUE"
)
@NamedQuery(
	name = "User.byUsername",
	query = "SELECT u FROM User u "
		  + "WHERE u.username = :username AND u.enabled = TRUE"
)
@NamedQuery(
	name = "User.hasUsername",
	query = "SELECT COUNT(u) "
		  + "FROM User u "
		  + "WHERE u.username = :username"
)
@NamedQuery(
	name = "User.getAllUsersExceptMe",
	query = "SELECT u FROM User u "
		  + "WHERE u.username != :username AND u.enabled = TRUE"
)
@Table(name = "IWUser")
public class User implements Serializable {

	/**
	 * Identifier
	 * 
	 * This @SequenceGenerator creates a sequence generator named
	 * "user_id_seq_gen" based on a sequence "user_id_seq" autocreated
	 * previously by the persistence provider, H2. This sequence will be used
	 * later to fill the "User.id" field.
	 * 
	 * Setting "allocationSize" to 1 allows the allocated sequence space to be
	 * just one, avoiding id gaps.
	 */
	@Id
	@SequenceGenerator(name = "user_id_seq_gen", sequenceName = "user_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq_gen")
	private long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	private String firstName;

	private String lastName;

	private boolean enabled;

	/**
	 * Roles
	 */
	public enum Role {
		USER, // normal users
		ADMIN, // admin users
	}

	private String roles; // split by ',' to separate roles

	/**
	 * Messages sent
	 */
	@OneToMany
	@JoinColumn(name = "sender_id")
	private List<Message> sentMessages = new ArrayList<>();

	/**
	 * Messages received
	 */
	@OneToMany
	@JoinColumn(name = "recipient_id")
	private List<Message> receivedMessages = new ArrayList<>();

	/**
	 * List of matches (MatchPlayer class) that the user has played
	 * 
	 * @see www.baeldung.com/jpa-many-to-many
	 * 
	 *      FIXME Esto debería ser "matches: List<Match>"...
	 */
	@OneToMany(mappedBy = "player")
	private List<MatchPlayer> matchPlayers = new ArrayList<>();

	/**
	 * List of friends (Friendship class)
	 * 
	 * Remainder: for each friendship, there are 2 instances: (u1, u2) y (u2, u1).
	 * That's why this list is mapped only to "user1"
	 * 
	 * @see www.baeldung.com/jpa-many-to-many
	 * 
	 *      FIXME Esto debería ser "friends: List<User>"...
	 */
	@OneToMany(mappedBy = "user1")
	private List<Friendship> friendships = new ArrayList<>();

	/**
	 * List of rooms that this user has joined (as RoomUser class)
	 * 
	 * @see www.baeldung.com/jpa-many-to-many
	 * 
	 *      FIXME Esto debería ser "rooms: List<Room>"...
	 */
	@ManyToMany
	private List<Room> rooms = new ArrayList<>();

	/**
	 * Checks whether this user has a given role.
	 * 
	 * @param role to check
	 * @return true iff this user has that role.
	 */
	public boolean hasRole(Role role) {
		String roleName = role.name();
		return Arrays.asList(roles.split(",")).contains(roleName);
	}
}
