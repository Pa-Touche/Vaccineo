package lu.vaccineo.test.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import lu.vaccineo.business.user.login.LoginRequest;
import lu.vaccineo.business.user.login.LoginResponse;
import lu.vaccineo.business.user.requests.CreateUserRequest;
import lu.vaccineo.test.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserIT extends AbstractBaseIT {

    public static final String USER_PATH = "/api/users";

    @Test
    public void user_can_create_account_and_login() throws Exception {
        // PREPARE
        String email = RandomDataUtils.emailAddress();
        String password = RandomDataUtils.password();

        // EXECUTE & CHECK
        createAccount(email, password);

        LoginResponse loginResponse = login(email, password);

        deleteUser(loginResponse);
    }

    private void createAccount(String email, String password) throws JsonProcessingException {
        CreateUserRequest request = CreateUserRequest.builder()
                .birthDate(LocalDate.now()) // Adjust if needed
                .email(email)
                .name(RandomDataUtils.lastName())
                .surname(RandomDataUtils.surname())
                .password(password)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = objectMapper.writeValueAsString(request);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<Integer> response = restTemplate.postForEntity(baseUrl + USER_PATH, entity, Integer.class);
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    private LoginResponse login(String email, String password) throws JsonProcessingException {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(
                objectMapper.writeValueAsString(loginRequest),
                headers
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/login",
                entity,
                String.class
        );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        // Optional: Deserialize response body
        LoginResponse loginResponse = objectMapper.readValue(response.getBody(), LoginResponse.class);

        assertThat(loginResponse).isNotNull();

        assertThat(loginResponse.getToken()).isNotBlank();

        return loginResponse;
    }

    private void deleteUser(LoginResponse loginResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + loginResponse.getToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                String.format("%s/api/users/%s", baseUrl, loginResponse.getUserId()),
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }
}
