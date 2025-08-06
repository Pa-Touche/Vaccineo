package lu.vaccineo.business.user.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.vaccineo.business.user.UserEntity;
import lu.vaccineo.business.user.UserPasswordEntity;
import lu.vaccineo.business.user.UserPasswordRepository;
import lu.vaccineo.business.user.exceptions.InvalidEmailException;
import lu.vaccineo.technical.security.JwtHelper;
import lu.vaccineo.technical.security.PasswordHelper;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final JwtHelper jwtHelper;
    private final PasswordHelper passwordHelper;
    private final UserPasswordRepository userPasswordRepository;

    public Optional<Integer> validCredentials(@NotNull LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        UserPasswordEntity entity = userPasswordRepository
                .findByUserEmail(email)
                .orElseThrow(() -> InvalidEmailException.forEmail(email));

        String expectedPasswordHash = passwordHelper.hashPassword(password, entity.getSalt());

        return Optional.of(entity.getUser())
                .map(UserEntity::getId)
                .filter(ignored -> expectedPasswordHash.equals(entity.getPasswordHash()));
    }

    public String generateToken(Integer userId) {
        return jwtHelper.generateToken(userId);
    }
}
