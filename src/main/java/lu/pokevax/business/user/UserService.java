package lu.pokevax.business.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.business.user.responses.UserResponse;
import lu.pokevax.technical.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * If possible split this class (+ controller) between plain user handling: create and security.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;
    private final UserCreationHelper userCreationHelper;


    public Integer createUser(@Valid CreateUserRequest request) {
        return userCreationHelper.createUser(request);
    }

    @Transactional(readOnly = true)
    public boolean emailExist(@NotNull String email) {
        return repository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserResponseOrThrowException(Integer id) {
        return userMapper.toResponse(getUserEntityOrThrowException(id));
    }

    @Transactional(readOnly = true)
    public UserEntity getUserEntityOrThrowException(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("The user with id: '%s' was not found", id)));
    }

    public void deleteUser(@NotNull Integer id) {
        repository.deleteById(id);
    }

}
