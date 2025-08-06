package lu.vaccineo.business.notification.projections;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class VaccineNotificationDto {
    private final LocalDate deadline;
    private final Integer doseNumber;
    private final String vaccineName;
}
