package lu.pokevax.business.notification.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.notification.VaccineNotificationRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpiredVaccineNotificationDeletionTasklet implements Tasklet {

    private final VaccineNotificationRepository repository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        int deletedCount = repository.deleteAllOlderThan(LocalDate.now());
        log.info("{} expired vaccine notifications were deleted", deletedCount);
        return RepeatStatus.FINISHED;
    }
}
