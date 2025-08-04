package lu.pokevax.business.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.notification.VaccineNotificationService;
import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.technical.security.PasswordHelper;
import lu.pokevax.technical.security.StorablePasswordWrapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

/**
 * The dependency to {@link VaccineNotificationService} is kept to keep things simple, but shouldn't stay, here are some alternatives:
 * - Usage of async:
 * - Queue system: Redis / Kafka that defined the instructions to build the notifications.
 * - Async (might fail, not atomic anymore)
 * - (Spring) Application Events: not performance-gain for better concern split.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreationHelper {

    private final PasswordHelper passwordHelper;
    private final UserPasswordRepository userPasswordRepository;
    private final UserMapper userMapper;

    // TODO: refactor: application events / async.
    private final VaccineNotificationService service;

    public Integer createUser(@Valid CreateUserRequest request) {
        UserEntity userEntity = userMapper.toEntity(request);

        StorablePasswordWrapper storablePassword = passwordHelper.generateStorablePassword(request.getPassword());

        UserPasswordEntity userPasswordEntity = UserPasswordEntity.builder()
                .user(userEntity)
                .salt(storablePassword.getSalt())
                .passwordHash(storablePassword.getHashedPassword())
                .build();

        save(userPasswordEntity);

        return userEntity.getId();
    }

    private UserPasswordEntity save(UserPasswordEntity userPasswordEntity) {
        return userPasswordRepository.save(userPasswordEntity);
    }
}
