package lu.vaccineo.business.vaccine.administered.validation;

import lombok.RequiredArgsConstructor;
import lu.vaccineo.business.vaccine.administered.AdministeredVaccineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ValidVaccineNameValidator implements ConstraintValidator<ValidVaccineName, String> {

    private final AdministeredVaccineService service;

    @Override
    public boolean isValid(String vaccineName, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(vaccineName)) {
            // NotNull already checked.
            return true;
        }

        return service.vaccineNameExists(vaccineName);
    }
}
