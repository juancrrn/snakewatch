package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Room user model key
 * 
 * @author Daniel Marín Irún
 * @author Juan Carrión Molina
 * @author Mohamed Ghanem
 * @author Óscar Caro Navarro
 * @author Óscar Molano Buitrago
 * 
 * @version 0.0.1
 */

@Embeddable // make this class a Many to Many relationship with JPA
// ToDo: implements methods of Serializable: hashcode() and equals()
public class RoomUserKey implements Serializable {

    /**
     * User identifier
     */

    @Column(name = "user_id")
    private Long userId; 

    /**
     * Room identifier
     */
    @Column(name = "room_id")     
    private Long roomId;
}