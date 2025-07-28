package lu.pokevax.business.vaccine;

import lombok.Getter;
import lombok.Setter;
import lu.pokevax.business.user.UserEntity;
import lu.pokevax.technical.utils.BaseEntity;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.OffsetDateTime;

@Entity(name = "vaccine")
@Getter
@Setter
public class AdministeredVaccine extends BaseEntity {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "vaccine_type_id", nullable = false, unique = true)
    @NotNull
    private VaccineType vaccineType;

    @Column(name = "user_id", nullable = false, unique = true)
    @NotNull
    private UserEntity user;

    @Column(name = "administration_date_time", nullable = false)
    @NotNull
    private OffsetDateTime administrationDateTime;

    @Positive
    @Column(name = "dose_number", nullable = false)
    @NotNull
    private Integer doseNumber;

    @Positive
    @Column(name = "comment", nullable = false)
    @Nullable
    private String comment;
}
