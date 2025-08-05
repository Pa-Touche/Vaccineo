package lu.pokevax.technical.security;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * This was implemented manually for learning-purposes.
 * Prefer using something like spring-security (see README).
 */
@Component
public class PasswordHelper {

    private static final SecureRandom RAND = new SecureRandom();
    private static final int ITERATIONS_NUMBER = 310_000; // 310_000 is recommended by OWASP
    private static final int KEY_LENGTH_BITS = 256;

    // The way the application is designed, this cannot be modified ad-hoc. Using Spring Security it's doable
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    public StorablePasswordWrapper generateStorablePassword(String rawPassword) {
        String salt = generateSalt();
        return StorablePasswordWrapper.builder()
                .salt(salt)
                .hashedPassword(hashPassword(rawPassword, salt))
                .build();
    }


    private static String generateSalt() {
        byte[] salt = new byte[16];
        RAND.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String hashPassword(String password, String salt) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(salt), ITERATIONS_NUMBER, KEY_LENGTH_BITS);
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}
