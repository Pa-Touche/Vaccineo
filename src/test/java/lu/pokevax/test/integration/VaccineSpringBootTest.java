package lu.pokevax.test.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VaccineSpringBootTest extends BaseSpringBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPostRequestWithBearerToken() throws Exception {
        String bearerToken = "randomBearerToken123"; // Random string as a bearer token

        mockMvc.perform(post("/vaccines")
                        .header("Authorization", "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()); // Adjust the expected status as needed
    }
}