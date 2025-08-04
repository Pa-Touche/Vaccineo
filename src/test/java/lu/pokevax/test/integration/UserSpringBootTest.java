package lu.pokevax.test.integration;

import lu.pokevax.business.user.UserController;
import lu.pokevax.business.user.login.LoginController;
import lu.pokevax.business.user.login.LoginRequest;
import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.business.user.responses.UserResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

public class UserSpringBootTest extends BaseSpringBootTest {

    @Autowired
    private UserController userController;

    @Autowired
    private LoginController loginController;

    @Test
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
                .isEqualTo(request);
    }

    @Test
    @Disabled("Create code to be able to create unique email addresses")
    void user_saved_and_login() {
        // PREPARE
        String email = "test@mail.ch";
        String password = "password";
        CreateUserRequest request = CreateUserRequest.builder()
                .birthDate(LocalDate.of(2000, 1, 1))
                .email(email)
                .name("Doe")
                .surname("John")
                .password(password)
                .build();

        // EXECUTE
        userController.createUser(request);

        // CHECK
        String actual = loginController.login(LoginRequest.builder()
                .email(email)
                .password(password)
                .build());

        Assertions.assertThat(actual)
                .isNotNull();
    }

    @Test
    void invalid_request() {
        // EXECUTE
        org.junit.jupiter.api.Assertions.assertThrows(ConstraintViolationException.class, () -> userController.createUser(new CreateUserRequest()));
    }

}
