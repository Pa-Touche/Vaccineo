package lu.pokevax.business.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.business.user.responses.UserResponse;
import lu.pokevax.technical.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * If possible split this class (+ controller) between plain user handling: create and security.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;

    public Integer createUser(@Valid CreateUserRequest request) {
        UserEntity entity = userMapper.toEntity(request);

        repository.save(entity);

        return entity.getId();
    }

    public UserResponse getUser(Integer id) {
        return repository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("The user with id: '{}' was not found", id)));
    }

    public void deleteUser(@NotNull Integer id) {
        repository.deleteById(id);
    }

}
