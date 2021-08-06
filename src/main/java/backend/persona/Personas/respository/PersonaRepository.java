package backend.persona.Personas.respository;

import backend.persona.Personas.entities.Personas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Personas, Integer> {

    Optional<Personas> findByFullname (String fullname);
}
