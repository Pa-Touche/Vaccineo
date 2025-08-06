package lu.vaccineo.business.vaccine.administered;

import lombok.*;
import lu.vaccineo.business.user.UserEntity;
import lu.vaccineo.business.vaccine.VaccineTypeEntity;
import lu.vaccineo.technical.persistence.LocalDateAttributeConverter;
import lu.vaccineo.technical.utils.BaseEntity;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

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
    private Integer id;

    /**
     * Eagerly loaded to avoid additional query.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vaccine_type_id")
    @NotNull
    private VaccineTypeEntity vaccineType;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    @NotNull
    private UserEntity user;

    @Column(name = "administration_date", nullable = false)
    @Convert(converter = LocalDateAttributeConverter.class)
    @NotNull
    @PastOrPresent
    private LocalDate administrationDate;

    @Positive
    @Column(name = "dose_number", nullable = false)
    @NotNull
    private Integer doseNumber;

    @Column(name = "comment")
    @Nullable
    private String comment;
}
