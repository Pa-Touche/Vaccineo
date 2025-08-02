package lu.pokevax.test.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Allows to perform some kind of white-box testing: as the spring boot context is accessible.
 * Here it's meant to be used to check controller flows.
 * <p>
 * To test security measures a WebContext should be started to check for filters etc.
 */
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@Tag("integration") // could be used for running some tests separatly
@Import(value = BaseSpringBootTest.CommonBaseConfiguration.class)
public class BaseSpringBootTest {

    @TestConfiguration
    @Slf4j
    public static class CommonBaseConfiguration {
        /**
         * Clean schema before executing migration
         * Must be in line with configuration application-test.yml
         */
        @Bean
        public FlywayMigrationStrategy clean() {
            return flyway -> {
                flyway.clean();
                flyway.migrate();
            };
        }


    }

}
