package lu.pokevax.test.integration;

import lu.pokevax.business.user.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserSpringBootTest extends BaseSpringBootTest {

    @Autowired
    private UserController userController;

    @Test
    void contextLoads() {
    }

}
