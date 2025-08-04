package lu.pokevax.test.integration;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VaccineSpringBootTest extends BaseSpringBootTest {

    @Test
    public void valid_bearer_token() throws Exception {
        mockMvc.perform(buildAuthorizedRequest(VACCINE_URI, 10)
                        .content("{}"))
                .andExpect(status().is2xxSuccessful());
    }


}
