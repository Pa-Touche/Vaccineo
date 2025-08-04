package lu.pokevax.business.vaccine;

import lombok.*;
import lu.pokevax.technical.utils.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Entity(name = "vaccine_schedule")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VaccineScheduleEntity extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "vaccine_type_id")
    private VaccineTypeEntity vaccineType;

    @Positive
    @Column(name = "dose_number", nullable = false)
    @NotNull
    private Integer doseNumber;

    @PositiveOrZero
    @Column(name = "application_deadline_days", nullable = false)
    @NotNull
    private Integer applicationDeadlineDays;
}
