package lu.pokevax.business.vaccine.administered;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.UserEntity;
import lu.pokevax.business.user.UserService;
import lu.pokevax.business.vaccine.VaccineTypeEntity;
import lu.pokevax.business.vaccine.VaccineTypeNameOnlyProjection;
import lu.pokevax.business.vaccine.VaccineTypeRepository;
import lu.pokevax.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import lu.pokevax.business.vaccine.administered.requests.SearchVaccineCriteria;
import lu.pokevax.business.vaccine.administered.requests.SearchVaccineRequest;
import lu.pokevax.business.vaccine.administered.responses.AdministeredVaccineResponse;
import lu.pokevax.technical.Tuple;
import lu.pokevax.technical.exceptions.ResourceNotFoundException;
import lu.pokevax.technical.web.UserIdWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdministeredVaccineService {
    private static final Tuple<List<String>, Sort.Direction> DEFAULT_SORT = new Tuple<>(
            Collections.singletonList(VaccineSortableField.ADMINISTRATION_DATE.getFieldName()),
            Sort.Direction.DESC
    );

    private final AdministeredVaccineRepository administeredVaccineRepository;
    private final VaccineTypeRepository vaccineTypeRepository;
    private final UserService userService;
    private final AdministeredVaccineMapper mapper;

    @Transactional(readOnly = true)
    public boolean vaccineNameExists(String vaccineName) {
        return vaccineTypeRepository.existsByName(vaccineName);
    }


    public Integer create(UserIdWrapper<CreateAdministeredVaccineRequest> requestWrapper) {
        CreateAdministeredVaccineRequest request = requestWrapper.getRequest();
        Integer userId = requestWrapper.getUserId();

        String vaccineName = request.getVaccineName();
        VaccineTypeEntity vaccineTypeEntity = vaccineTypeRepository.findByName(vaccineName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No vaccine with name: '%s' was found", vaccineName)));


        UserEntity userEntity = userService.getUserEntityOrThrowException(userId);

        AdministeredVaccineEntity administeredVaccineEntity = AdministeredVaccineEntity.builder()
                .vaccineType(vaccineTypeEntity)
                .user(userEntity)
                .doseNumber(request.getDoseNumber())
                .administrationDate(request.getAdministrationDate())
                .comment(request.getComment())
                .build();

        return administeredVaccineRepository.save(administeredVaccineEntity).getId();
    }

    @Transactional(readOnly = true)
    public List<AdministeredVaccineResponse> search(UserIdWrapper<SearchVaccineRequest> requestWrapper) {
        SearchVaccineRequest request = requestWrapper.getRequest();
        Integer userId = requestWrapper.getUserId();


        Specification<AdministeredVaccineEntity> spec = Specification.where(null);

        spec = spec.and(AdministeredVaccineSpecifications.hasUserId(userId));

        if (request != null && request.getCriteria() != null) {
            SearchVaccineCriteria criteria = request.getCriteria();

            spec = spec.and(AdministeredVaccineSpecifications.hasVaccineName(criteria.getVaccineName()));
            spec = spec.and(AdministeredVaccineSpecifications.hasAdministrationDate(criteria.getAdministrationDate()));
            spec = spec.and(AdministeredVaccineSpecifications.hasDoseNumber(criteria.getDoseNumber()));
        }


        Tuple<List<String>, Sort.Direction> sortRequest = Optional.ofNullable(request)
                .map(SearchVaccineRequest::getSort)
                .map(originalSortRequest -> {
                    Sort.Direction direction = originalSortRequest.getDirection();
                    List<VaccineSortableField> fields = originalSortRequest.getFields();
                    List<String> sortableFields = CollectionUtils.isNotEmpty(fields) ? fields.stream()
                            .map(VaccineSortableField::getFieldName)
                            .collect(Collectors.toList()) : new ArrayList<>(DEFAULT_SORT.getFirst());

                    // provides a fallback in case vaccines sort-criteria are exactly equal. (same date).
                    sortableFields.add("id");

                    return new Tuple<>(sortableFields, direction);
                })
                .orElse(DEFAULT_SORT);


        return administeredVaccineRepository.findAll(spec, Sort.by(sortRequest.getSecond(), sortRequest.getFirst().toArray(new String[0]))).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

    }

    public List<String> retrieveVaccineTypes() {
        return vaccineTypeRepository.findAllProjectedBy().stream()
                .map(VaccineTypeNameOnlyProjection::getName)
                .collect(Collectors.toList());
    }
}
