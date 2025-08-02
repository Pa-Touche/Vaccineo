package lu.pokevax.business.user.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.UserPasswordEntity;
import lu.pokevax.business.user.UserPasswordRepository;
import lu.pokevax.business.user.exceptions.InvalidEmailException;
import lu.pokevax.technical.security.JwtHelper;
import lu.pokevax.technical.security.PasswordHelper;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final JwtHelper jwtHelper;
    private final PasswordHelper passwordHelper;
    private final UserPasswordRepository userPasswordRepository;

    public boolean validCredentials(@NotNull LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        UserPasswordEntity entity = userPasswordRepository
                .findByUserEmail(email)
                .orElseThrow(() -> InvalidEmailException.forEmail(email));

        String expectedPasswordHash = passwordHelper.hashPassword(password, entity.getSalt());

        return expectedPasswordHash.equals(entity.getPasswordHash());
    }

    public String generateToken(String email) {
        return jwtHelper.generateToken(email);
    }
}
