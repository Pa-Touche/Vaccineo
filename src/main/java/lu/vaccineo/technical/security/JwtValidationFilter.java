package lu.vaccineo.technical.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.vaccineo.business.user.UserController;
import lu.vaccineo.business.user.login.LoginController;
import lu.vaccineo.technical.web.WebTokenExtractor;
import org.apache.commons.lang3.Strings;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtValidationFilter implements Filter {

    private final WebTokenExtractor webTokenExtractor;

    @Override
    public void init(FilterConfig filterConfig) {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        // every API called must be checked for the presence of a token except user creation and login.
        if (LoginController.URI.equals(requestURI)
                || (UserController.URI.equals(requestURI) && HttpMethod.POST.name().equals(request.getMethod()))
                || !Strings.CS.contains(requestURI, "/api")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Optional<Integer> userId = webTokenExtractor.extractUserId(request);

        if (!userId.isPresent()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided or is invalid. It's might be expired, try to connect again using the '/login' endpoint");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // nothing to Do
    }
}
