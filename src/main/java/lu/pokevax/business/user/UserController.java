package lu.pokevax.business.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.business.user.responses.UserResponse;
import lu.pokevax.technical.ValidatedRestController;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@ValidatedRestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    @GetMapping(":{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return service.getUser(id);
    }

    @PostMapping
    public void createUser(@RequestBody @Valid CreateUserRequest request) {
        log.debug("createUser: [{}]", request);

        service.createUser(request);
    }


    @DeleteMapping
    public void deleteUser(@RequestBody @Valid CreateUserRequest request) {
        log.debug("deleteUser: [{}]", request);

        service.createUser(request);
    }
}
