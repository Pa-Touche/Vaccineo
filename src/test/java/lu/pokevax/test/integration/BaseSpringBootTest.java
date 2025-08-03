package lu.pokevax.test.integration;

import lombok.extern.slf4j.Slf4j;
import lu.pokevax.technical.security.JwtHelper;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Allows to perform some kind of white-box testing: as the spring boot context is accessible.
 * Here it's meant to be used to check controller flows.
 * <p>
 * To test security measures a WebContext should be started to check for filters etc.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
@Tag("springBoot") // could be used for running some tests separatly
@Import(value = BaseSpringBootTest.CommonBaseConfiguration.class)
public class BaseSpringBootTest {

    protected MockMvc mockMvc;

    @Autowired
    private JwtHelper jwtHelper;

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
