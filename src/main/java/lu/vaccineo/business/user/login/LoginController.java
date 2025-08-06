package lu.vaccineo.business.user.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.vaccineo.business.user.exceptions.InvalidPasswordException;
import lu.vaccineo.technical.ValidatedRestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@ValidatedRestController
@RequestMapping(LoginController.URI)
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    public static final String URI = "/api/login";

    private final LoginService service;

    @PostMapping
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return service.validCredentials(request)
                .map(userId -> LoginResponse.builder()
                        .token(service.generateToken(userId))
                        .userId(userId)
                        .build())
                .orElseThrow(() -> new InvalidPasswordException(String.format("Invalid password provided for email: '%s'", request.getEmail())));
    }
}
