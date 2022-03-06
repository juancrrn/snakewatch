package es.ucm.fdi.iw.model;


import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User report model
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
public class UserReport /*implements Transferable<Room.Transfer>*/ {	
	
	/**
	 * Identifier
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

	/**
	 * User that issued the report 
	 */
    @ManyToOne
    @JoinColumn(name="reportingUser_id")
    private User reportingUser;

	/**
	 * User being reported
	 */
    @ManyToOne
    @JoinColumn(name="reportedUser_id")
    private User reportedUser;

	/**
	 * Moderator in charge of taken decisions
	 */
    @ManyToOne
    @JoinColumn(name="moderatorUser_id")
    private User moderatorUser;

	/**
	 * Explanation given by reportingUser
	 */
    private String reasons;

    /**
     * Status of the report
     * 
     * - Sent: reported but not yet examined by the moderator
     * - Being Processed: currently being examined by the moderator
     * - Closed: decision already taken (punishment or dismissal)
     */
    public enum Status {
        SENT,
        BEING_PROCESSED,
        CLOSED
    }

    @Column(nullable = false)
    private Status status;

    /*
    @Getter
    @AllArgsConstructor
    private static class Transfer {
        private long id;
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