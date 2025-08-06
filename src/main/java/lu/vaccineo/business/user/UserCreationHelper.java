package lu.vaccineo.business.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.vaccineo.business.notification.VaccineNotificationCreationHelper;
import lu.vaccineo.business.notification.VaccineNotificationService;
import lu.vaccineo.business.user.requests.CreateUserRequest;
import lu.vaccineo.technical.security.PasswordHelper;
import lu.vaccineo.technical.security.StorablePasswordWrapper;
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
    private final VaccineNotificationCreationHelper vaccineNotificationCreationHelper;

    public Integer createUser(@Valid CreateUserRequest request) {
        UserEntity userEntity = userMapper.toEntity(request);

        StorablePasswordWrapper storablePassword = passwordHelper.generateStorablePassword(request.getPassword());

        UserPasswordEntity userPasswordEntity = UserPasswordEntity.builder()
                .user(userEntity)
                .salt(storablePassword.getSalt())
                .passwordHash(storablePassword.getHashedPassword())
                .build();

        save(userPasswordEntity);

        // this call will slow done the user creation. see TODOs and this class's JavaDoc
        vaccineNotificationCreationHelper.createVaccineNotifications(userEntity);

        return userEntity.getId();
    }

    private UserPasswordEntity save(UserPasswordEntity userPasswordEntity) {
        return userPasswordRepository.save(userPasswordEntity);
    }
}
