package backend.persona.service.Impl;

import backend.persona.Personas.entities.Personas;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.data.domain.Sort;
import backend.persona.Personas.exceptions.ObjetoExistenteException;
import backend.persona.Personas.exceptions.ObjetoNoExisteException;
import backend.persona.Personas.respository.PersonaRepository;
import backend.persona.Personas.service.Imple.PersonaServiceImple;
import backend.persona.Personas.service.PersonaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class personaServiceImplTest {

    @Mock
    private static PersonaRepository personaRepository;

    @Mock
    private static LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

    @InjectMocks
    private static PersonaServiceImple personaServiceImple;

    private final static String NOMBRE_NO_EXISTENTE = "XXX";
    private final static String PRINCIPAL = "Hector";
    private final static String SUPLENTE = "Sanchez";
    private final static LocalDate FECHA_NACIMIENTO = LocalDate.now();


    @Test
    void cuando_crea_y_nombre_no_existe_deberia_crear_persona() {
        when(personaRepository.findByFullname(NOMBRE_NO_EXISTENTE)).thenReturn(Optional.ofNullable(null));
        var persona = this.crearPersona(0, NOMBRE_NO_EXISTENTE, FECHA_NACIMIENTO);

        personaServiceImple.create(persona);

        verify(personaRepository, times(1))
                .findByFullname(persona.getFullname());
        verify(personaRepository, times(1))
                .save(persona);
    }

    @Test
    void cuando_crea_y_nombre_existe_deberia_lanzar_exception() {
        var persona = this.crearPersona(0, PRINCIPAL, FECHA_NACIMIENTO);
        this.configurarPersonaExistente(PRINCIPAL);

        assertThrows(ObjetoExistenteException.class, () ->  personaServiceImple.create(persona));

        verify(personaRepository, times(1))
                .findByFullname(persona.getFullname());
        verify(personaRepository, times(0))
                .save(persona);
    }

    @Test
    void cuando_actualiza_y_persona_no_existe_deberia_lanzar_exception() {
        var persona = this.crearPersona(10, PRINCIPAL, FECHA_NACIMIENTO);
        when(personaRepository.findById(persona.getId())).thenReturn(Optional.ofNullable(null));

        assertThrows(RuntimeException.class, () -> personaServiceImple.create(persona));

        verify(personaRepository, times(1))
                .findById(persona.getId());
        verify(personaRepository, times(0))
                .save(persona);
    }

    @Test
    void cuando_actualiza_y_nombre_existe_deberia_lanzar_exception() {
        var persona = this.crearPersona(10, PRINCIPAL, FECHA_NACIMIENTO);
        this.configurarPersonaExistente(PRINCIPAL);
        when(personaRepository.findById(persona.getId())).thenReturn(Optional.of(persona));

        assertThrows(ObjetoExistenteException.class, () -> personaServiceImple.update(persona));

        verify(personaRepository, times(1))
                .findById(persona.getId());
        verify(personaRepository, times(1))
                .findByFullname(PRINCIPAL);
        verify(personaRepository, times(0))
                .save(persona);
    }

    @Test
    void cuando_actualiza_con_mismo_nombre() {
        var persona = this.crearPersona(10, PRINCIPAL, FECHA_NACIMIENTO);
        when(personaRepository.findById(persona.getId())).thenReturn(Optional.of(persona));
        when(personaRepository.findByFullname(PRINCIPAL)).thenReturn(Optional.of(persona));

        personaServiceImple.create(persona);

        verify(personaRepository, times(1))
                .findById(persona.getId());
        verify(personaRepository, times(1))
                .findByFullname(PRINCIPAL);
        verify(personaRepository, times(1))
                .save(persona);
    }

    @Test
    void cuando_existe_persona_buscar_por_id() {
        var persona = this.crearPersona(10, PRINCIPAL, FECHA_NACIMIENTO);

        when(personaRepository.findById(persona.getId()))
                .thenReturn(Optional.of(persona));

        var personaEncontrado = personaServiceImple.searchId(persona.getId());
        assertThat(personaEncontrado).isNotNull();
        assertThat(personaEncontrado.getId()).isEqualTo(10);
    }

    @Test
    void cuando_no_existe_persona_buscar_por_id() {
        Optional<Personas> personaNoExistente = Optional.ofNullable(null);
        when(personaRepository.findById(5)).thenReturn(personaNoExistente);

        assertThrows(ObjetoNoExisteException.class, () -> personaServiceImple.searchId(5));

        verify(personaRepository, times(1)).findById(5);
    }


    @Test
    void listar() {
        Sort sort = Sort.by("id");
        when(personaRepository.findAll(sort)).thenReturn(this.obtenerListaPersonas());

        List<Personas> personas = personaServiceImple.list();

        assertThat(personas).isNotNull().isNotEmpty();
        verify(personaRepository, times(1)).findAll(sort);
    }

    @Test
    void cuando_borrar_persona_existe() {
        var persona = this.crearPersona(10, PRINCIPAL, FECHA_NACIMIENTO);
        Optional<Personas> personaExistente = Optional.of(persona);

        when(personaRepository.findById(10)).thenReturn(personaExistente);

        personaServiceImple.delete(10);
        verify(personaRepository, times(1)).deleteById(10);
    }

    @Test
    void cuando_borrar_persona_no_existe() {
        var persona = this.crearPersona(10, PRINCIPAL, FECHA_NACIMIENTO);
        Optional<Personas> personaExistente = Optional.ofNullable(null);

        when(personaRepository.findById(10)).thenReturn(personaExistente);

        assertThrows(ObjetoNoExisteException.class, () -> personaServiceImple.delete(10));
        verify(personaRepository, times(0)).deleteById(10);
        verify(personaRepository, times(1)).findById(10);
    }



    private Personas crearPersona(int id, String fullname, LocalDate birth) {
        var persona = new Personas();
        persona.setId(id);
        persona.setFullname(fullname);
        persona.setBirth(birth);
        return persona;
    }

    private List<Personas> obtenerListaPersonas() {
        var persona1 = this.crearPersona(1, PRINCIPAL, FECHA_NACIMIENTO);
        var persona2 = this.crearPersona(2, SUPLENTE, FECHA_NACIMIENTO);
        return Arrays.asList(persona1, persona2);
    }

    private void configurarPersonaExistente(String fullname) {
        var personaExistenteConMismoNombre = new Personas();
        personaExistenteConMismoNombre.setId(100);
        personaExistenteConMismoNombre.setFullname(fullname);
        when(personaRepository.findByFullname(fullname))
                .thenReturn(Optional.of(personaExistenteConMismoNombre));
    }


}
