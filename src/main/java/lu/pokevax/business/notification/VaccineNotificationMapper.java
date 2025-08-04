package lu.pokevax.business.notification;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VaccineNotificationMapper {


    @Mapping(target = "vaccineName", source = "vaccineScheduleEntity.vaccineType.name")
    @Mapping(target = "doseNumber", source = "vaccineScheduleEntity.doseNumber")
    VaccineNotificationResponse toResponse(VaccineNotificationEntity entity);
}
