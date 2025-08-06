package lu.vaccineo.business.notification.batch;

import lu.vaccineo.business.notification.VaccineNotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ScheduleConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ScheduleConfiguration.class))
            .withBean(VaccineNotificationRepository.class, () -> mock(VaccineNotificationRepository.class));

    @Test
    void shouldCreateCleanupJobBean_whenEnableCronIsTrue() {
        contextRunner
                .withPropertyValues("vaccineo.enableCron=true")
                .run(context -> assertThat(context)
                        .hasSingleBean(ExpiredVaccineNotificationCleanupJob.class));
    }

    @Test
    void shouldCreateCleanupJobBean_whenEnableCronIsMissing() {
        contextRunner
                .run(context -> assertThat(context)
                        .hasSingleBean(ExpiredVaccineNotificationCleanupJob.class));
    }

    @Test
    void shouldNotCreateCleanupJobBean_whenEnableCronIsFalse() {
        // PREPARE
        contextRunner
                .withPropertyValues("vaccineo.enableCron=false")
                .run(context -> assertThat(context)
                        .doesNotHaveBean(ExpiredVaccineNotificationCleanupJob.class));
    }
}