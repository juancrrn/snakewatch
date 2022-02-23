package es.ucm.fdi.iw.model;

/**
 * Room user model
 * 
 * Many users to a single room relationship, users that are in a room. The same
 * user cannot be in different rooms at the same time.
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
public class RoomUserKey implements Serializable{

    @Column(name = "user_id")
    private Long userId; 
    @Column(name = "room_id")     
    private Long roomId;      

}