package lu.pokevax.business.vaccine.administered;

import lombok.*;
import lu.pokevax.business.user.UserEntity;
import lu.pokevax.business.vaccine.VaccineTypeEntity;
import lu.pokevax.technical.utils.BaseEntity;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.OffsetDateTime;

@Entity(name = "vaccine_administered")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministeredVaccineEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "vaccine_type_id")
    @NotNull
    private VaccineTypeEntity vaccineType;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @NotNull
    private UserEntity user;

    @Column(name = "administration_date_time", nullable = false)
    @NotNull
    private OffsetDateTime administrationDateTime;

    @Positive
    @Column(name = "dose_number", nullable = false)
    @NotNull
    private Integer doseNumber;

    @Column(name = "comment", nullable = false)
    @Nullable
    private String comment;
}
