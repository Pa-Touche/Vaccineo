package lu.vaccineo.business.vaccine.administered;

import lu.vaccineo.business.vaccine.administered.responses.AdministeredVaccineResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AdministeredVaccineMapper {

    @Mapping(target = "vaccineName", source = "vaccineType.name")
    AdministeredVaccineResponse toResponse(AdministeredVaccineEntity entity);
}
