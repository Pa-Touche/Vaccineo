package lu.pokevax.technical.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.user.login.LoginController;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtValidationFilter implements Filter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int PREFIX_LENGTH = BEARER_PREFIX.length();
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtHelper jwtHelper;

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("JwtValidationFilter init: [{}]", filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getRequestURI().equals(LoginController.LOGIN_URI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or invalid");
            return;
        }

        String token = authorizationHeader.substring(PREFIX_LENGTH);

        if (!jwtHelper.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The provided token is invalid. It's might be expired, try to connect again using the '/login' endpoint");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.debug("JwtValidationFilter was destroyed");
    }
}
