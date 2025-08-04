package lu.pokevax.test.integration;

import lu.pokevax.business.user.login.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecuritySpringBootTest extends BaseSpringBootTest {

    @Test
    public void invalid_bearer_token() throws Exception {
        String bearerToken = "randomBearerToken123"; // Random string as a bearer token

        mockMvc.perform(post(USER_URI)
                        .header("Authorization", "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void other_token_type() throws Exception {
        mockMvc.perform(post(USER_URI)
                        .header("Authorization", "This is not really a Bearer !")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void missing_bearer_token() throws Exception {
        mockMvc.perform(post(VACCINE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void valid_bearer_token() throws Exception {
        String validToken = jwtHelper.generateToken(50);

        mockMvc.perform(post(USER_URI)
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void invalid_login_unknown_email() throws Exception {
        mockMvc.perform(post(LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(LoginRequest.builder()
                                .email("valid@mail.de")
                                .password("some_password")
                                .build())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void invalid_login_incorrect_password() throws Exception {
        CreateUserSummary randomUser = createRandomUser();

        mockMvc.perform(post(LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(LoginRequest.builder()
                                .email(randomUser.getEmail())
                                .password("***some_password***")
                                .build())))
                .andExpect(status().isUnauthorized());
    }
}