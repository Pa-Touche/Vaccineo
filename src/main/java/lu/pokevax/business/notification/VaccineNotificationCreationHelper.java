package lu.pokevax.business.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.UserEntity;
import lu.pokevax.business.vaccine.VaccineScheduleRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class VaccineNotificationCreationHelper {
    private final VaccineNotificationRepository vaccineNotificationRepository;
    private final VaccineScheduleRepository vaccineScheduleRepository;

    /**
     * Creates the notifications when the user is created.
     * Another approach could be to make it lazy: on first connect / fetching of vaccine list etc.
     *
     * @param user currently created user.
     */
    public void createVaccineNotifications(UserEntity user) {
        // this is generally unsafe.
        LocalDate now = LocalDate.now();
        vaccineNotificationRepository.saveAll(vaccineScheduleRepository.findAll().stream()
                .map(scheduleEntity -> VaccineNotificationEntity.builder()
                        .user(user)
                        .vaccineScheduleEntity(scheduleEntity)
                        .deadline(user.getBirthDate().plusDays(scheduleEntity.getApplicationDeadlineDays()))
                        .build())
                .filter(vaccineNotificationEntity -> vaccineNotificationEntity.getDeadline().isAfter(now))
                .collect(Collectors.toList())
        );
    }
}
