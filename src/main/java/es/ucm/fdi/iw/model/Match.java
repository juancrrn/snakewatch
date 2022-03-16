package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.time.*;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Match model
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
public class Match /*implements Transferable<Room.Transfer>*/ {	
	
	/**
	 * Identifier
	 * 
	 * <p> This SequenceGenerator creates a sequence generator named
	 * "match_id_seq_gen" based on a sequence "match_id_seq" autocreated
	 * previously by the persistence provider, H2. 
     * 
     * <p> This sequence will be used later to fill the "Match.id" field.
	 * 
	 * <p> Setting "allocationSize" to 1 allows the allocated sequence space to be
	 * just one, avoiding id gaps.
	 */
    @Id
	@SequenceGenerator(name = "match_id_seq_gen", sequenceName = "match_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_id_seq_gen")
    private long id;

    /**
     * Room
     */
    @ManyToOne
    private Room room;

    /**
     * Level
     */
    @ManyToOne
    private Level level;


	/**
	 * List of players (MatchPlayer class) that have participated in this match
	 * @see www.baeldung.com/jpa-many-to-many
	 */
    @OneToMany(mappedBy = "match")
    private List<MatchPlayer> players = new ArrayList<>();



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
     * <p> If null, the match has no maximum.
     * <p> TODO: check this constraint when adding new players to the match
     */
    private int maxPlayers;


    private LocalDate date;

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