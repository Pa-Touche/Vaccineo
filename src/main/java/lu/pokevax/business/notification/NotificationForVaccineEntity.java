package lu.pokevax.business.notification;

import lombok.*;
import lu.pokevax.business.user.UserEntity;
import lu.pokevax.business.vaccine.VaccineScheduleEntity;
import lu.pokevax.technical.utils.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Each element can be seen as reminder for a user to perform a specific vaccine.
 */
@Entity(name = "notification_vaccine")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "expirationDate"})
public class NotificationForVaccineEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_schedule_id")
    private VaccineScheduleEntity vaccineScheduleEntity;

    /**
     * Once this date is passed, the notification must be removed (marked for deletion)
     * Persisted to avoid re-computing birthDate and Vaccine schedule end - date.
     */
    @FutureOrPresent
    @Column(name = "expiration_date", nullable = false)
    @NotNull
    private LocalDate expirationDate;


}
