package lu.vaccineo.business.notification.batch;

import lu.vaccineo.business.notification.VaccineNotificationRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        value = "vaccineo.enableCron",
        havingValue = "true",
        matchIfMissing = true)
public class ScheduleConfiguration {

    @Bean
    public ExpiredVaccineNotificationCleanupJob expiredVaccineNotificationCleanupJob(VaccineNotificationRepository repository) {
        return new ExpiredVaccineNotificationCleanupJob(repository);
    }
}
