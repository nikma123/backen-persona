package backend.persona.Personas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import backend.persona.Personas.entities.Personas;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PersonasDto {

    private int id;

    @NotNull(message = "El nombre no puede ser vacío")
    @Size(min = 1, max = 255, message = "El nombre debe tener mínimo un carácter y máximo 255")
    private String fullname;


    @NotNull(message = "La fecha de nacimiento  no puede ser vacío")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate birth;

}



