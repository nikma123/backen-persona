package backend.persona.Personas.service.Imple;


import backend.persona.Personas.entities.Personas;
import backend.persona.Personas.exceptions.ObjetoExistenteException;
import backend.persona.Personas.exceptions.ObjetoNoExisteException;
import backend.persona.Personas.respository.PersonaRepository;
import backend.persona.Personas.service.PersonaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class PersonaServiceImple implements PersonaService {

    private PersonaRepository personaRepository;
    private Validator validator;

    public PersonaServiceImple(PersonaRepository personaRepository, Validator validator) {
        this.personaRepository = personaRepository;
        this.validator = validator;
    }

    @Override
    public Personas create(Personas personas) {
        this.validar(personas);
        this.personaRepository.save(personas);
        return personas;
    }

    @Override
    public Personas update(Personas personas) {
        this.searchId(personas.getId());
        this.validar(personas);
        this.personaRepository.save(personas);
        return personas;
    }

    @Override
    public Personas searchId(int id) {
        return this.personaRepository
                .findById(id)
                .orElseThrow(() -> new ObjetoNoExisteException("No existe la persona con id " + id));
    }

    @Override
    public List<Personas> list() {
        return this.personaRepository.findAll(Sort.by("id"));
    }

    @Override
    public void delete(int id) {
        this.searchId(id);
        this.personaRepository.deleteById(id);
    }

    private void validar(Personas persona) {
        Set<ConstraintViolation<Personas>> violations = validator.validate(persona);
        if (!violations.isEmpty()) {
            var sb = new StringBuilder();
            for (ConstraintViolation<Personas> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb, violations);
        }
        this.validateFullName(persona);
    }

    private void validateFullName(Personas persona) {
        Optional<Personas> exists = this.personaRepository.findByFullname(persona.getFullname());
        if (exists.isPresent() && !exists.get().check(persona)) {
            throw new ObjetoExistenteException("Ya existe la persona " + persona.getFullname());
        }
    }
}
