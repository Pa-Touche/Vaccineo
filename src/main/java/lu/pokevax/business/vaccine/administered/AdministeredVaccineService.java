package lu.pokevax.business.vaccine.administered;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.UserEntity;
import lu.pokevax.business.user.UserService;
import lu.pokevax.business.vaccine.VaccineTypeEntity;
import lu.pokevax.business.vaccine.VaccineTypeRepository;
import lu.pokevax.technical.exceptions.ResourceNotFoundException;
import lu.pokevax.technical.web.UserIdWrapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdministeredVaccineService {
    private final AdministeredVaccineRepository administeredVaccineRepository;
    private final VaccineTypeRepository vaccineTypeRepository;
    private final UserService userService;

    public Integer create(UserIdWrapper<CreateAdministeredVaccineRequest> requestWrapper) {
        log.debug("create: [{}]", requestWrapper);

        CreateAdministeredVaccineRequest request = requestWrapper.getRequest();
        Integer userId = request.getUserId();

        String vaccineName = request.getVaccineName();
        VaccineTypeEntity vaccineTypeEntity = vaccineTypeRepository.findByName(vaccineName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No vaccine with name: '%s' was found", vaccineName)));


        UserEntity userEntity = userService.getUserEntityOrThrowException(userId);

        AdministeredVaccineEntity administeredVaccineEntity = AdministeredVaccineEntity.builder()
                .vaccineType(vaccineTypeEntity)
                .user(userEntity)
                .doseNumber(request.getDoseNumber())
                .administrationDateTime(request.getAdministrationDateTime())
                .comment(request.getComment())
                .build();

        return administeredVaccineRepository.save(administeredVaccineEntity).getId();
    }
}
