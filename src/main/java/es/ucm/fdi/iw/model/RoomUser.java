package es.ucm.fdi.iw.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

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
public class RoomUser{

    /**
     * Identifier
     */
    @EmbeddedId
    RoomUserKey id;

    /**
     * User
     */
    @ManyToOne
    @MapsId("userId")
    private User user; 

    /**
     * Room
     */
    @ManyToOne
    @MapsId("roomId")
    private Room room;      

    /**
     * User is admin of the room
     */
    private boolean admin;

}