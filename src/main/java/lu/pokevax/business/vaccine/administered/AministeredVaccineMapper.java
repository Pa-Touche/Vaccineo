package lu.pokevax.business.vaccine.administered;

import lu.pokevax.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import lu.pokevax.business.vaccine.administered.responses.AdministeredVaccineResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AministeredVaccineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "vaccineType", ignore = true)
    AdministeredVaccineEntity toEntity(CreateAdministeredVaccineRequest request);

    @Mapping(target = "vaccineName", source = "vaccineType.name")
    AdministeredVaccineResponse toResponse(AdministeredVaccineEntity entity);
}
