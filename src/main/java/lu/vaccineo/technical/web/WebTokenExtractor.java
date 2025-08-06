package lu.vaccineo.technical.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.vaccineo.technical.security.JwtHelper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

/**
 * This helps to extract the JWT token from the request.
 *
 * @implNote This type of class is needed as Spring-Security is not used.
 * Spring-Security provides a nicer API and much more features.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WebTokenExtractor {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int PREFIX_LENGTH = BEARER_PREFIX.length();
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtHelper jwtHelper;

    public Optional<Integer> extractUserId(@NotNull HttpServletRequest httpServletRequest) {
        log.trace("extractUserId: [{}]", httpServletRequest);

        return Optional.ofNullable(httpServletRequest.getHeader(AUTHORIZATION_HEADER))
                .filter(Objects::nonNull)
                .filter(authorizationHeader -> authorizationHeader.startsWith(BEARER_PREFIX))
                .map(authorizationHeader -> authorizationHeader.substring(PREFIX_LENGTH))
                .filter(jwtHelper::validateToken)
                .map(jwtHelper::extractUserIdFromToken);
    }

    public Integer extractUserIdOrThrowException(@NotNull HttpServletRequest httpServletRequest) {
        return extractUserId(httpServletRequest)
                .orElseThrow(() -> new MissingUserIdInTokenException("No user id could be extracted from the token"));
    }

    public <T> UserIdWrapper<T> enrichRequestWithUserID(
            @NotNull HttpServletRequest httpServletRequest,
            T request) {
        Integer userId = extractUserIdOrThrowException(httpServletRequest);

        log.trace("Extracted userId: [{}]", userId);

        return UserIdWrapper.<T>builder()
                .userId(userId)
                .request(request)
                .build();
    }
}
