package es.ucm.fdi.iw.model;

import javax.persistence.*;


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
     * Compound key with reference to: User + Room
     */
    @EmbeddedId
    @Column(nullable=false)
    private RoomUserKey id;

    /**
     * User is admin of the room
     */
    private boolean admin;

}