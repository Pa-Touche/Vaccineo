package lu.pokevax.ui;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@ConfigurationProperties(prefix = "pokevax")
@Data
public class Configuration {

    /**
     * TODO: check how must be used:
     * - When creating the reminder entity
     * - On batch computation.
     * Defines how many days before the actual deadline.
     *
     * Enhancements: could be configured for each user / vaccine (or both).
     */
    public Integer numberOfDaysBeforeVaccineScheduleExpiration;
}
