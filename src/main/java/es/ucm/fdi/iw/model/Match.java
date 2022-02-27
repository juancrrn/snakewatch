package es.ucm.fdi.iw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Match /*implements Transferable<Room.Transfer>*/ {	
	
	/**
	 * Identifier
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    /**
     * Room
     * 
     * TODO Check relationship type
     */
    @OneToOne
    private Room room;

    /**
     * Level
     * 
     * TODO Check relationship type
     */
    @OneToOne
    private Level level;

    /**
     * Status
     * 
     * - Waiting
     * - Ongoing
     * - Paused
     * - Ended
     */
    public enum Status {
        WAITING,
        ONGOING,
        PAUSED,
        ENDED
    }

    @Column(nullable = false)
    private Status status;

    /**
     * Maximum number of players
     * 
     * If null, the match has no maximum.
     */
    private int maxPlayers;

    /**
     * Winner
     * 
     * Might be null if the match is waiting, ongoing or paused, or if it has
     * ended and has no winner.
     */
    @ManyToOne
    private User winner;

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