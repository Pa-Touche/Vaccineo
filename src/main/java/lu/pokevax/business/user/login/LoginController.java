package lu.pokevax.business.user.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.exceptions.InvalidPasswordException;
import lu.pokevax.technical.ValidatedRestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@ValidatedRestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService service;

    @PostMapping
    public String login(@RequestBody @Valid LoginRequest request) {
        if (service.validCredentials(request)) {
            return service.generateToken(request.getEmail());
        } else {
            throw new InvalidPasswordException(request.getEmail());
        }
    }
}
