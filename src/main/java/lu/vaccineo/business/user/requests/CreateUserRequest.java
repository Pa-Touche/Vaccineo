package lu.vaccineo.business.user.requests;

import lombok.*;
import lu.vaccineo.business.user.validation.UniqueUserEmail;

import javax.validation.constraints.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
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
    @UniqueUserEmail
    private String email;

    @Pattern(regexp = ".{5,}")
    @NotEmpty
    private String password;
}
