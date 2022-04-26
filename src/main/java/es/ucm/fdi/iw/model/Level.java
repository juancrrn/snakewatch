package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Level model
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
@NamedQuery(
    name = "Level.getAll",
    query = "SELECT l FROM Level l"
)

public class Level implements Serializable {

	/**
	 * Identifier
	 * 
	 * <p>
	 * This SequenceGenerator creates a sequence generator named
	 * "level_id_seq_gen" based on a sequence "level_id_seq" autocreated
	 * previously by the persistence provider, H2.
	 * 
	 * <p>
	 * This sequence will be used later to fill the "Level.id" field.
	 * 
	 * <p>
	 * Setting "allocationSize" to 1 allows the allocated sequence space to be
	 * just one, avoiding id gaps.
	 */
	@Id
	@SequenceGenerator(name = "level_id_seq_gen", sequenceName = "level_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "level_id_seq_gen")
	private long id;
	
	@OneToMany(mappedBy = "level")
    private List<UserLevel> userLevels = new ArrayList<>();
}