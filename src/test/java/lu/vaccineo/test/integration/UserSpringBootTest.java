package lu.vaccineo.test.integration;

import lombok.SneakyThrows;
import lu.vaccineo.business.user.UserController;
import lu.vaccineo.business.user.login.LoginController;
import lu.vaccineo.business.user.login.LoginRequest;
import lu.vaccineo.business.user.requests.CreateUserRequest;
import lu.vaccineo.business.user.responses.UserResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserSpringBootTest extends BaseSpringBootTest {

    @Autowired
    private UserController userController;

    @Autowired
    private LoginController loginController;

    @Test
    @DisplayName("Check user information is properly stored: implemented in white-box manner to simplify creation checks")
    void user_can_be_saved_and_retrieved() {
        // PREPARE
        CreateUserRequest request = CreateUserRequest.builder()
                .birthDate(LocalDate.of(2000, 1, 1))
                .email("test@mail.ch")
                .name("Doe")
                .surname("John")
                .password("password")
                .build();

        // EXECUTE
        Integer createdUserId = userController.createUser(request);

        // CHECK
        UserResponse actual = userController.getUser(createdUserId);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(request);
    }

    @SneakyThrows
    @Test
    void user_delete_user() {
        // PREPARE
        CreateUserSummary user = createRandomUser();

        // EXECUTE
        Integer userId = user.getUserId();
        mockMvc.perform(buildAuthorizedDeleteRequest(uriWithIdentifier(USER_URI, userId), userId))
                .andExpect(status().is2xxSuccessful());

        // CHECK
        mockMvc.perform(buildAuthorizedGetRequest(uriWithIdentifier(USER_URI, userId), userId))
                .andExpect(status().isNotFound());
    }


    @SneakyThrows
    @Test
    void user_saved_and_login() {
        // PREPARE
        CreateUserSummary createdUser = createRandomUser();

        // EXECUTE & CHECK
        mockMvc.perform(post(LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(LoginRequest.builder()
                                .email(createdUser.getEmail())
                                .password(createdUser.getPassword())
                                .build()))
                )
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void user_saved_and_duplicateEmail() {
        // PREPARE
        CreateUserRequest request = CreateUserRequest.builder()
                .birthDate(LocalDate.of(1993, 4, 29))
                .email("mail@mail.fr")
                .name("name")
                .surname("surname")
                .password("password")
                .build();

        createUser(request);

        // EXECUTE & CHECK
        // same request for a second time
        mockMvc.perform(post(USER_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void invalid_request_empty_payload() {
        // TODO: rewrite using mockMVC
        // EXECUTE
        org.junit.jupiter.api.Assertions.assertThrows(ConstraintViolationException.class, () -> userController.createUser(new CreateUserRequest()));
    }

}
