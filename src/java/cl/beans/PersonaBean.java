package cl.beans;

import cl.model.Persona;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;

/**
 *
 * @author LeoGuitar
 */
@Singleton
public class PersonaBean implements PersonaBeanLocal {

    private List<Persona> listaPersonas = new ArrayList<>();

    public PersonaBean() {
        listaPersonas.add(new Persona("1-9", "Juan", "Administrador", "jp@gmail.com", "asdf", true));
        listaPersonas.add(new Persona("2-8", "Pedro", "Persona", "jp@gmail.com", "asdf", true));
        listaPersonas.add(new Persona("3-7", "Diego", "Persona", "jp@gmail.com", "asdf", false));
        listaPersonas.add(new Persona("4-6", "María", "Persona", "jp@gmail.com", "asdf", true));
    }
    
    @Override
    public Persona buscar(String rut) {
        for (Persona p : listaPersonas) {
            if (rut.equals(p.getRut())) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Persona loguear(String rut, String clave) {
        for (Persona p : listaPersonas) {
            if (rut.equals(p.getRut()) && clave.equals(p.getClave())) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void editar(Persona p) {
        Persona personaEditada = buscar(p.getRut());
        personaEditada.setActivo(p.isActivo());
    }

    @Override
    public List<Persona> getPersonaList() {
        return listaPersonas;
    }

    @Override
    public String add(Persona persona) {
        Persona aux = buscar(persona.getRut());
        if (aux == null) {
            listaPersonas.add(persona);
            return "Persona creada exitosamente!";
        } else {
            return "Rut ingresado ya se encuentra registrado..";
        }
    }
    
}