package lu.pokevax.test.integration;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.ZoneId;

@UtilityClass
public class RandomDataUtils {
    private static final Faker faker = new Faker();

    public static LocalDate birthDate() {
        return faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String emailAddress() {
        return faker.internet().emailAddress();
    }

    public static String password() {
        // TODO: this is not nice
        return faker.name().fullName();
    }

    public static String lastName() {
        return faker.name().lastName();
    }

    public static String surname() {
        return faker.name().firstName();
    }
}
