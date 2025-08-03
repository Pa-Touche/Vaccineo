package lu.pokevax.business.user;

import lu.pokevax.business.user.requests.CreateUserRequest;
import lu.pokevax.business.user.responses.UserResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {
    // TODO: must be defined

    UserResponse toResponse(UserEntity user);


    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(CreateUserRequest request);
}
