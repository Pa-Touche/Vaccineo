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
@RequestMapping(LoginController.URI)
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    public static final String URI = "/login";

    private final LoginService service;

    @PostMapping
    public String login(@RequestBody @Valid LoginRequest request) {
        return service.validCredentials(request)
                .map(service::generateToken)
                .orElseThrow(() -> new InvalidPasswordException(String.format("Invalid password provided for email: '%s'", request.getEmail())));
    }
}
