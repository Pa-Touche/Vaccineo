package lu.vaccineo.business.vaccine.administered.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Only performs checks if present, must be used combined with @NotNull / @NotEmpty if field is required.
 */
@Documented
@Constraint(validatedBy = ValidVaccineNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidVaccineName {
    String message() default "Vaccine name is not known in the system";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

