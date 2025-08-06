package lu.vaccineo.technical.web;

import lu.vaccineo.technical.security.JwtHelper;
import lu.vaccineo.test.unit.BaseUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

public class WebTokenExtractorTest extends BaseUnitTest {

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private WebTokenExtractor victim;

    @Test
    void extractUserId_shouldReturnUserId_whenValidBearerToken() {
        // PREPARE
        String token = "valid.jwt.token";
        int expectedUserId = 123;
        String headerValue = "Bearer " + token;

        when(httpServletRequest.getHeader(WebTokenExtractor.AUTHORIZATION_HEADER)).thenReturn(headerValue);
        when(jwtHelper.validateToken(token)).thenReturn(true);
        when(jwtHelper.extractUserIdFromToken(token)).thenReturn(expectedUserId);

        // EXECUTE
        Optional<Integer> result = victim.extractUserId(httpServletRequest);

        // CHECK
        assertThat(result).contains(expectedUserId);
    }

    @Test
    void extractUserId_shouldReturnEmpty_whenNoAuthorizationHeader() {
        // PREPARE
        when(httpServletRequest.getHeader(WebTokenExtractor.AUTHORIZATION_HEADER)).thenReturn(null);

        // EXECUTE
        Optional<Integer> result = victim.extractUserId(httpServletRequest);

        // CHECK
        assertThat(result).isEmpty();
    }

    @Test
    void extractUserId_shouldReturnEmpty_whenHeaderDoesNotStartWithBearer() {
        // PREPARE
        when(httpServletRequest.getHeader(WebTokenExtractor.AUTHORIZATION_HEADER)).thenReturn("Token abc.def.ghi");

        // EXECUTE
        Optional<Integer> result = victim.extractUserId(httpServletRequest);

        // CHECK
        assertThat(result).isEmpty();
    }

    @Test
    void extractUserId_shouldReturnEmpty_whenTokenIsInvalid() {
        // PREPARE
        String token = "invalid.jwt.token";
        when(httpServletRequest.getHeader(WebTokenExtractor.AUTHORIZATION_HEADER)).thenReturn("Bearer " + token);
        when(jwtHelper.validateToken(token)).thenReturn(false);

        // EXECUTE
        Optional<Integer> result = victim.extractUserId(httpServletRequest);

        // CHECK
        assertThat(result).isEmpty();
    }

    @Test
    void extractUserIdOrThrowException_shouldThrow_whenNoUserIdFound() {
        // PREPARE
        when(httpServletRequest.getHeader(WebTokenExtractor.AUTHORIZATION_HEADER)).thenReturn(null);

        // EXECUTE & CHECK
        assertThatThrownBy(() -> victim.extractUserIdOrThrowException(httpServletRequest))
                .isInstanceOf(MissingUserIdInTokenException.class)
                .hasMessageContaining("No user id could be extracted");
    }

    @Test
    void enrichRequestWithUserID_shouldReturnUserIdWrapper_whenTokenIsValid() {
        // PREPARE
        String token = "valid.jwt.token";
        int userId = 456;
        String body = "payload";

        when(httpServletRequest.getHeader(WebTokenExtractor.AUTHORIZATION_HEADER)).thenReturn("Bearer " + token);
        when(jwtHelper.validateToken(token)).thenReturn(true);
        when(jwtHelper.extractUserIdFromToken(token)).thenReturn(userId);

        // EXECUTE
        UserIdWrapper<String> result = victim.enrichRequestWithUserID(httpServletRequest, body);

        // CHECK
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getRequest()).isEqualTo(body);
    }
}
