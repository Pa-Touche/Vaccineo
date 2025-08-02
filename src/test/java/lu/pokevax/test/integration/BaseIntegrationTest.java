package lu.pokevax.test.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@Tag("integration") // could be used for running some tests separatly
public class BaseIntegrationTest {

}
