package lu.pokevax.business.vaccine;

import lombok.Getter;
import lombok.Setter;
import lu.pokevax.technical.utils.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity(name = "vaccine_type")
@Getter
@Setter
public class VaccineTypeEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @Positive
    @Column(name = "number_of_doses", nullable = false)
    @NotNull
    private Integer numberOfDoses;

    @Column(name = "treatment_description", nullable = false)
    @NotNull
    private String treatmentDescription;

}
