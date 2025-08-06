package lu.vaccineo.business.user.validation;

import lombok.RequiredArgsConstructor;
import lu.vaccineo.business.user.UserService;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

    private final UserService service;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !service.emailExist(email);
    }
}
