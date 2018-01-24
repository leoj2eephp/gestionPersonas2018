package cl.beans;

import cl.model.Persona;
import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author LeoGuitar
 */
@Local
public interface PersonaBeanLocal {
    
    Persona buscar(String rut);
    
    Persona loguear(String rut, String clave);
    
    void editar(Persona p);
    
    List<Persona> getPersonaList();
    
    String add(Persona persona);
    
}