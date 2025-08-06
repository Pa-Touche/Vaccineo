package lu.vaccineo.business.vaccine.administered.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Only performs checks if present, must be used combined with @NotNull / @NotEmpty if field is required.
 */
@Documented
@Constraint(validatedBy = VaccineAlreadyAdministeredValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VaccineAlreadyAdministered {
    String message() default "Le vaccin (type et numéro de dose) a déjà été administré";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

