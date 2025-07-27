package lu.pokevax;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "vaccine")
@Getter
@Setter
public class VaccineEntity {

    @Id
    private long id;

    @Column
    private String name;

}
