package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Friendship model
 * 
 * Many users to many users relationship.
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
public class Friendship implements Serializable{

    @Id
    @ManyToOne
    @MapsId("userId")
    private User user1;

    @Id
    @ManyToOne
    @MapsId("userId")
    private User user2;
}