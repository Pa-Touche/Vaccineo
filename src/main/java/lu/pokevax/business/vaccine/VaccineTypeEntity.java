package lu.pokevax.business.vaccine;

import lombok.Getter;
import lombok.Setter;
import lu.pokevax.technical.utils.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity(name = "vaccine")
@Getter
@Setter
public class VaccineTypeEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "vaccine_type", nullable = false, unique = true)
    @NotNull
    private VaccineType vaccineType;


    @Column(name = "name", nullable = false, unique = true)
    @NotNull
    private String name;


    @Positive
    @Column(name = "number_of_doses", nullable = false)
    @NotNull
    private Integer numberOfDoses;

    @Column(name = "treatment_description", nullable = false, unique = true)
    @NotNull
    private String treatmentDescription;

}
