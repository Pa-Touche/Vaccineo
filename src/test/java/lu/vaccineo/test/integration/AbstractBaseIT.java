package lu.vaccineo.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Tag;
import org.springframework.web.client.RestTemplate;

/**
 * Simplified Spring-less base for integration tests.
 */
@Tag("integration")
public abstract class AbstractBaseIT {

    protected final RestTemplate restTemplate = new RestTemplate();
    protected final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    protected final String baseUrl = "http://localhost:8080";

}
