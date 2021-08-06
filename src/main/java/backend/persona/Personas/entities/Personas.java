package backend.persona.Personas.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Personas extends BaseEntities{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private int id;

    @Column(name = "fullname_person")
    @NotNull(message = "el dato nombre no puede ser vacio")
    @Size(min = 1, max = 255, message = "El nombre debe tener mínimo un carácter y máximo 255")
    private String fullname;

    @Column(name = "birth_person")
    @NotNull(message = "El dato fecha no puede ser vacio")
    private LocalDate birth;


}
