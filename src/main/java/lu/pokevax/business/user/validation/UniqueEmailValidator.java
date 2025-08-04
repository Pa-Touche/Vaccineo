package lu.pokevax.business.user.validation;

import lu.pokevax.business.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

    @Autowired
    private UserService service;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !service.emailExist(email);
    }
}
