package lu.pokevax.business.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.technical.security.PasswordHelper;
import lu.pokevax.technical.security.StorablePasswordWrapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreationHelper {

    private final PasswordHelper passwordHelper;
    private final UserPasswordRepository userPasswordRepository;
    private final UserMapper userMapper;

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
