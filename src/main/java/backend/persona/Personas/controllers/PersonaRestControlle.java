package backend.persona.Personas.controllers;


import backend.persona.Personas.dto.PersonasDto;
import backend.persona.Personas.entities.Personas;
import backend.persona.Personas.service.PersonaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/personaprueba", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200/")
public class PersonaRestControlle {

    private PersonaService personaService;
    private ModelMapper modelMapper;

    @Autowired
    public PersonaRestControlle(PersonaService personaService, ModelMapper modelMapper) {
        this.personaService = personaService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<PersonasDto>> listar() {
        List<PersonasDto> person = this.personaService.list()
                .stream()
                .map(persona -> modelMapper.map(persona, PersonasDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PersonasDto> create(@Valid @RequestBody PersonasDto personasDto) {
        var person = this.modelMapper.map(personasDto, Personas.class);
        person = this.personaService.create(person);
        return new ResponseEntity<>(modelMapper.map(person, PersonasDto.class), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonasDto> update(@Valid @RequestBody PersonasDto personasDto,
                                                 @PathVariable("id") int id) {
        if (personasDto == null || personasDto.getId() != id) {
            throw new RuntimeException("El id de la persona a actualizar no corresponde al path asosicado");
        }
        var person = this.modelMapper.map(personasDto, Personas.class);
        person = this.personaService.update(person);
        return new ResponseEntity<>(modelMapper.map(person, PersonasDto.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") int id,
                                   @PathVariable("id") int idUsuario) {
        personaService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
