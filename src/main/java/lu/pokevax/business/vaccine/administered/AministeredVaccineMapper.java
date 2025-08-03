package lu.pokevax.business.vaccine.administered;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AministeredVaccineMapper {

    // TODO: define & common config.
    // AdministeredVaccineEntity from(CreateAdministeredVaccineRequest request);
}
