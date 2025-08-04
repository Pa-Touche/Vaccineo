package lu.pokevax.business.notification.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ExpiredVaccineNotificationsBatchConfiguration {
    @Bean
    public Job deleteExpiredVaccineNotificationsJob(JobBuilderFactory jobs, Step deleteOldVaccinesStep) {
        return jobs.get("deleteExpiredVaccineNotificationsJob")
                .start(deleteOldVaccinesStep)
                .build();
    }

    @Bean
    public Step deleteExpiredVaccineNotificationsStep(StepBuilderFactory steps, ExpiredVaccineNotificationDeletionTasklet tasklet) {
        return steps.get("deleteExpiredVaccineNotificationsStep")
                .tasklet(tasklet)
                .build();
    }
}
