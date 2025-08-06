package lu.vaccineo.business.notification.batch;

import lu.vaccineo.business.notification.VaccineNotificationRepository;
import lu.vaccineo.test.unit.BaseUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExpiredVaccineNotificationCleanupJobTest extends BaseUnitTest {

    @Mock
    private VaccineNotificationRepository repository;

    @InjectMocks
    private ExpiredVaccineNotificationCleanupJob victim;

    @Test
    public void deleteExpiredVaccineNotifications_shouldDeleteOldEntriesAndLogCount() {
        // PREPARE
        LocalDate now = LocalDate.now();
        int expectedDeletedCount = 5;
        when(repository.deleteAllOlderThan(now)).thenReturn(expectedDeletedCount);

        // EXECUTE
        victim.deleteExpiredVaccineNotifications();

        // CHECK
        verify(repository).deleteAllOlderThan(now);
    }
}