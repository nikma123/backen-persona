package backend.persona.Personas.service;

import backend.persona.Personas.entities.Personas;

import java.util.List;

public interface PersonaService {

    Personas create(Personas personas);

    Personas update(Personas persona);

    Personas searchId(int id);

    List<Personas> list();

    void delete(int id);

}
