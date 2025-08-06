package lu.vaccineo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vaccineo")
@Data
public class Configuration {

    /**
     * TODO: check how must be used:
     * - When creating the reminder entity
     * - On batch computation.
     * Defines how many days before the actual deadline.
     * <p>
     * Enhancements: could be configured for each user / vaccine (or both).
     */
    private Integer numberOfDaysBeforeVaccineScheduleExpiration;

    private Long tokenExpirationTimeMillis;

}
