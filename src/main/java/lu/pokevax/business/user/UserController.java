package lu.pokevax.business.user;


import lombok.RequiredArgsConstructor;
import lu.pokevax.business.user.exceptions.InvalidPasswordException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        if (service.validCredentials(loginRequest)) {
            return service.generateToken(loginRequest.getEmail());
        } else {
            throw new InvalidPasswordException(loginRequest.getEmail());
        }
    }
}
