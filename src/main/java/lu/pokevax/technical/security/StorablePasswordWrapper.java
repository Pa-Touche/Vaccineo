package lu.pokevax.technical.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StorablePasswordWrapper {
    private final String hashedPassword;
    private final String salt;
}
