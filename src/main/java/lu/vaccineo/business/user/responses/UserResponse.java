package lu.vaccineo.business.user.responses;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@Builder
public class UserResponse {
    @NotNull
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @PastOrPresent
    @NotNull
    private LocalDate birthDate;

    @Email
    @NotEmpty
    private String email;
}
