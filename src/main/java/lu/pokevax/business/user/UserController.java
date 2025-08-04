package lu.pokevax.business.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.business.user.responses.UserResponse;
import lu.pokevax.technical.ValidatedRestController;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ValidatedRestController
@RequestMapping(UserController.URI)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    public static final String URI = "/users";

    private final UserService service;

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable @NotNull Integer id) {
        log.debug("getUser with id: [{}]", id);
        return service.getUserResponseOrThrowException(id);
    }

    @PostMapping
    public Integer createUser(@RequestBody @Valid CreateUserRequest request) {
        log.debug("createUser: [{}]", request);

        return service.createUser(request);
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable @NotNull Integer id) {
        log.debug("deleteUser with id: [{}]", id);

        service.deleteUser(id);
    }
}
