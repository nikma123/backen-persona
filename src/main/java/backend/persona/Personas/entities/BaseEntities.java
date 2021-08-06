package backend.persona.Personas.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntities {

    public abstract int getId();

    @Column(name = "created")
    @CreatedDate
    private LocalDateTime created;

    @Column(name = "modified")
    @LastModifiedDate
    private LocalDateTime modified;


    public boolean check(BaseEntities o) {
        return (o == this || (o.getId() == this.getId()));
    }
}
