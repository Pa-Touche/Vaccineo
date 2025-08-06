package lu.vaccineo.business.vaccine.administered.validation;

import lombok.RequiredArgsConstructor;
import lu.vaccineo.business.vaccine.administered.AdministeredVaccineService;
import lu.vaccineo.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class VaccineAlreadyAdministeredValidator implements ConstraintValidator<VaccineAlreadyAdministered, CreateAdministeredVaccineRequest> {

    private final AdministeredVaccineService service;

    @Override
    public boolean isValid(CreateAdministeredVaccineRequest request, ConstraintValidatorContext context) {
        return !service.alreadyAdministered(request.getVaccineName(), request.getDoseNumber());
    }
}
