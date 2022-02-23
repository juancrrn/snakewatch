package es.ucm.fdi.iw.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.Data;
import lombok.NoArgsConstructor;

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

@Entity
@Data
@NoArgsConstructor
public class RoomUser{

    @EmbeddedId
    RoomUserKey roomUserKey;

    @Column(name = "user_id")
    @ManyToOne
    @MapsId("studentId")
    private User user; 

    @ManyToOne
    @MapsId("studentId")
    @Column(name = "room_id")     
    private Room room;      

    private boolean admin;

}