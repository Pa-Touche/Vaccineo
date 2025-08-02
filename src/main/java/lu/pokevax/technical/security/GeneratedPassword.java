package lu.pokevax.technical.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneratedPassword {
    private final String hashedPassword;
    private final String salt;
}
