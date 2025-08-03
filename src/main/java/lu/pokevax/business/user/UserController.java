package lu.pokevax.business.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.business.user.responses.UserResponse;
import lu.pokevax.technical.ValidatedRestController;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ValidatedRestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    @GetMapping(":{id}")
    public UserResponse getUser(@PathVariable @NotNull Integer id) {
        return service.getUser(id);
    }

    @PostMapping
    public Integer createUser(@RequestBody @Valid CreateUserRequest request) {
        log.debug("createUser: [{}]", request);

        return service.createUser(request);
    }


    @DeleteMapping(":{id}")
    public void deleteUser(@PathVariable @NotNull Integer id) {
        log.debug("deleteUser with id: [{}]", id);

        service.deleteUser(id);
    }

    public static void main(String[] args) {

        System.out.println(LocalDate.of(1950, 1, 1).toEpochDay());

        System.out.println(LocalDate.parse(LocalDate.now() + ""));
    }
}
