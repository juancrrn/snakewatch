package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; 

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room; 
    
}