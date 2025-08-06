package lu.vaccineo.business.user.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUserEmail {
    String message() default "Cette addresse mail est déjà utilisé";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

