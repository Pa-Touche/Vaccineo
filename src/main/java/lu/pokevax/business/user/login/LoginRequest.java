package lu.pokevax.business.user.login;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Builder
@ToString(of = "email") // do not log passwords.
public class LoginRequest {
    @Email
    @NotEmpty
    private String email;

    @Pattern(regexp = ".{5,}")
    @NotEmpty
    private String password;
}
