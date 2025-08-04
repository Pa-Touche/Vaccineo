package lu.pokevax.business.notification.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.notification.VaccineNotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Slf4j
public class ExpiredVaccineNotificationCleanupJob {

    private final VaccineNotificationRepository repository;

    @Scheduled(cron = "${pokevax.expiredVaccineNotificationDeletionCron}")
    @Transactional
    public void deleteExpiredVaccineNotifications() {
        LocalDate cutoffDate = LocalDate.now();

        int deletedCount = repository.deleteAllOlderThan(cutoffDate);

        log.info("{} expired vaccine notifications were deleted", deletedCount);
    }
}
