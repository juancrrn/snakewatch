package es.ucm.fdi.iw.model;

/**
 * Transferable interface
 * 
 * Used to JSON-ize objects.
 * 
 * Pending deprecation if Jackson works.
 * 
 * @author Daniel Marín Irún
 * @author Juan Carrión Molina
 * @author Mohamed Ghanem
 * @author Óscar Caro Navarro
 * @author Óscar Molano Buitrago
 * 
 * @version 0.0.1
 */
@Deprecated
public interface Transferable<T> {
    T toTransfer();
}
