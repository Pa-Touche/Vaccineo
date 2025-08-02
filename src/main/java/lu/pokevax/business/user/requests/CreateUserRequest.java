package lu.pokevax.business.user.requests;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@ToString(exclude = "password") // other information should probably also NOT be logged
public class CreateUserRequest {

    @NotNull
    @Pattern(regexp = ".{2,}")
    private String name;

    @NotNull
    @Pattern(regexp = ".{2,}")
    private String surname;

    @PastOrPresent
    @NotNull
    private LocalDate birthDate;

    @Email
    @NotEmpty
    private String email;

    @Pattern(regexp = ".{5,}")
    @NotEmpty
    private String password;
}
