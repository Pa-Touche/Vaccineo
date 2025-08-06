package lu.pokevax.business.notification.projections;

import java.time.LocalDate;


public interface VaccineNotificationProjection {
    VaccineScheduleProjection getVaccineSchedule();

    LocalDate getDeadline();
}
