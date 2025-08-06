package lu.vaccineo.business.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VaccineNotificationResponse {
    private String vaccineName;

    private Integer doseNumber;

    private LocalDate deadline;
}
