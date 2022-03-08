package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An authorized user of the system.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name="User.byUsername",
                query="SELECT u FROM User u "
                        + "WHERE u.username = :username AND u.enabled = TRUE"),
        @NamedQuery(name="User.hasUsername",
                query="SELECT COUNT(u) "
                        + "FROM User u "
                        + "WHERE u.username = :username"),
		@NamedQuery(name="User.getUsersLessMe",
				query="SELECT u FROM User u "
				+ "WHERE u.username != :username AND u.enabled = TRUE")
		
})
@Table(name="IWUser")
public class User /*implements Transferable<User.Transfer>*/ {

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
	
	/**
	 * Username
	 */
    @Column(nullable = false, unique = true)
    private String username;
	
	/**
	 * Password
	 */
    @Column(nullable = false)
    private String password;
	
	/**
	 * First name
	 */
    private String firstName;
	
	/**
	 * Last name
	 */
    private String lastName;
	
	/**
	 * Enabled
	 */
    private boolean enabled;
	
	/**
	 * Roles
	 */
    public enum Role {
        USER,			// normal users 
        ADMIN,          // admin users
    }

    private String roles; // split by ',' to separate roles
	
	/**
	 * Messages sent
	 */
	@OneToMany
	@JoinColumn(name = "sender_id")
	private List<Message> sent = new ArrayList<>();
	
	/**
	 * Messages received
	 */
	@OneToMany
	@JoinColumn(name = "recipient_id")	
	private List<Message> received = new ArrayList<>();		

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

	/*
    @Getter
    @AllArgsConstructor
    public static class Transfer {
		private long id;
        private String username;
		private int totalReceived;
		private int totalSent;
    }

	@Override
    public Transfer toTransfer() {
		return new Transfer(id,	username, received.size(), sent.size());
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}
	*/
}

