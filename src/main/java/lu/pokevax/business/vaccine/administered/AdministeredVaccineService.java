package lu.pokevax.business.vaccine.administered;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.UserEntity;
import lu.pokevax.business.user.UserService;
import lu.pokevax.business.vaccine.VaccineTypeEntity;
import lu.pokevax.business.vaccine.VaccineTypeRepository;
import lu.pokevax.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import lu.pokevax.business.vaccine.administered.requests.SearchVaccineCriteria;
import lu.pokevax.business.vaccine.administered.requests.SearchVaccineRequest;
import lu.pokevax.business.vaccine.administered.requests.SortRequest;
import lu.pokevax.business.vaccine.administered.responses.AdministeredVaccineResponse;
import lu.pokevax.technical.exceptions.ResourceNotFoundException;
import lu.pokevax.technical.web.UserIdWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdministeredVaccineService {
    public static final SortRequest DEFAULT_SORT = SortRequest.builder()
            .fields(Collections.singletonList("administrationDate"))
            .direction(Sort.Direction.DESC)
            .build();

    private final AdministeredVaccineRepository administeredVaccineRepository;
    private final VaccineTypeRepository vaccineTypeRepository;
    private final UserService userService;
    private final AministeredVaccineMapper mapper;

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

    public List<AdministeredVaccineResponse> search(UserIdWrapper<SearchVaccineRequest> requestWrapper) {
        SearchVaccineRequest request = requestWrapper.getRequest();
        Integer userId = requestWrapper.getUserId();

        SearchVaccineCriteria criteria = request.getCriteria();

        Specification<AdministeredVaccineEntity> spec = Specification.where(null);

        spec = spec.and(AdministeredVaccineSpecifications.hasUserId(userId));

        if (criteria != null) {
            spec = spec.and(AdministeredVaccineSpecifications.hasVaccineName(criteria.getVaccineName()));
            spec = spec.and(AdministeredVaccineSpecifications.hasAdministrationDate(criteria.getAdministrationDate()));
            spec = spec.and(AdministeredVaccineSpecifications.hasDoseNumber(criteria.getDoseNumber()));
            spec = spec.and(AdministeredVaccineSpecifications.hasCommentContaining(criteria.getComment()));
        }


        SortRequest sortRequest = Optional.ofNullable(request.getSort())
                .map(originalSortRequest -> {
                    Sort.Direction direction = originalSortRequest.getDirection();
                    List<String> fields = originalSortRequest.getFields();
                    List<String> sortableFields = CollectionUtils.isNotEmpty(fields) ? new ArrayList<>(fields) : new ArrayList<>(DEFAULT_SORT.getFields());

                    // provides a fallback in case vaccines sort-criteria are exactly equal. (same date).
                    sortableFields.add("id");

                    return SortRequest.builder()
                            .direction(direction != null ? direction : DEFAULT_SORT.getDirection())
                            .fields(sortableFields)
                            .build();
                })
                .orElse(DEFAULT_SORT);


        return administeredVaccineRepository.findAll(spec, Sort.by(sortRequest.getDirection(), sortRequest.getFields().toArray(new String[0]))).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

    }
}
