package lu.pokevax.business.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.business.user.responses.UserResponse;
import org.springframework.stereotype.Service;

import javax.validation.Valid;


/**
 * If possible split this class (+ controller) between plain user handling: create and security.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;

    public void createUser(@Valid CreateUserRequest request) {

    }

    public UserResponse getUser(Long id) {
        return null;
    }
}
