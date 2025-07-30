package lu.pokevax.business.vaccine;

import lombok.Getter;
import lombok.Setter;
import lu.pokevax.technical.utils.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Entity(name = "vaccine_schedule")
@Getter
@Setter
public class VaccineScheduleEntity extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_type_id")
    private VaccineTypeEntity vaccineType;

    @Positive
    @Column(name = "dose_number", nullable = false)
    @NotNull
    private Integer doseNumber;

    @PositiveOrZero
    @Column(name = "lower_applicability_days", nullable = false)
    @NotNull
    private Integer lowerApplicabilityDays;

    @PositiveOrZero
    @Column(name = "upperApplicabilityDays", nullable = false)
    @NotNull
    private Integer upperApplicabilityDays;
}
