package lu.vaccineo.business.notification;

import lu.vaccineo.business.notification.projections.VaccineNotificationDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VaccineNotificationMapper {


    VaccineNotificationResponse toResponse(VaccineNotificationDto entity);
}
