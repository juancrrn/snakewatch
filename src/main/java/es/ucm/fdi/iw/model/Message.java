package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import lombok.Data;

/**
 * Message model
 * 
 * A message that users can send to each other.
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
@NamedQueries({
	@NamedQuery(name="Message.countUnread",
	query="SELECT COUNT(m) FROM Message m "
			+ "WHERE m.recipient.id = :userId AND m.dateRead = null")
})
@Data
public class Message {
	
	//private static Logger log = LogManager.getLogger(Message.class);	
	
	/**
	 * Identifier
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;

	/**
	 * Sender
	 */
	@ManyToOne
	private User sender;

	/**
	 * Recipient
	 */
	@ManyToOne
	private User recipient;

	/**
	 * Text
	 */
	private String text;
		
	/**
	 * Date sent
	 */
	private LocalDateTime dateSent;
		
	/**
	 * Date read
	 */
	private LocalDateTime dateRead;
}
