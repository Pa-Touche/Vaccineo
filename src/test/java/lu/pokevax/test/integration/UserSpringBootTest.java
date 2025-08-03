package lu.pokevax.test.integration;

import lu.pokevax.business.user.UserController;
import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.business.user.responses.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class UserSpringBootTest extends BaseSpringBootTest {

    @Autowired
    private UserController userController;

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
        UserResponse user = userController.getUser(createdUserId);

        System.out.println("user = " + user);
    }

}
