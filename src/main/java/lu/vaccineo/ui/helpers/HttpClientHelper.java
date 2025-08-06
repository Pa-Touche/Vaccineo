package lu.vaccineo.ui.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

// TODO: contains too much duplication
@Component
@RequiredArgsConstructor
@Slf4j
public class HttpClientHelper {

    private final ObjectMapper objectMapper;
    private final SessionWrapper sessionWrapper;

    public <T, R> HttpResponse<R> post(PostRequest<T, R> request) {
        try {
            URL url = new URL(getBaseUrl() + request.getEndpoint());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(HttpMethod.POST.name());
            conn.setDoOutput(true);
            conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE);

            if (sessionWrapper.isAuthenticated()) {
                conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + sessionWrapper.getToken());
            }

            String json = objectMapper.writeValueAsString(request.getBody());
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();

            if (code >= 200 && code < 300) {
                try (InputStreamReader reader = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)) {
                    return HttpResponse.<R>builder()
                            .data(objectMapper.readValue(reader, request.getReturnType()))
                            .build();
                }
            } else {
                log.error("Backend responded with error: [{}]", code);
                return HttpResponse.<R>builder()
                        .errorMessage("It seems that the server could not process your request." + "\n" + IOUtils.toString(conn.getErrorStream(), StandardCharsets.UTF_8))
                        .build();
            }


        } catch (Exception e) {
            log.error("JSON POST failed", e);
            return HttpResponse.<R>builder()
                    .errorMessage("An unexpected error occured, sorry for the inconvenience")
                    .build();
        }
    }

    public static String getBaseUrl() {
        VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
        HttpServletRequest request = ((VaadinServletRequest) vaadinRequest).getHttpServletRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        return url.replace(uri, "") + contextPath;       // → http://localhost:8080
    }

    public <R> HttpResponse<R> get(GetRequest<R> request) {
        try {
            // this was added for async purposes.
            String spec;
            if (StringUtils.isNotEmpty(request.getFullEndpoint())) {
                spec = request.getFullEndpoint();
            } else {
                spec = getBaseUrl() + request.getEndpoint();
            }

            URL url = new URL(spec);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(HttpMethod.GET.name());
            conn.setDoOutput(true);

            String requestToken = request.getToken();
            if (StringUtils.isNotEmpty(requestToken) || sessionWrapper.isAuthenticated()) {
                conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + Optional.ofNullable(requestToken)
                        .filter(StringUtils::isNotEmpty)
                        .orElseGet(sessionWrapper::getToken));
            }

            int code = conn.getResponseCode();

            if (code >= 200 && code < 300) {
                try (InputStreamReader reader = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)) {
                    return HttpResponse.<R>builder()
                            .data(objectMapper.readValue(reader, request.getReturnType()))
                            .build();
                }
            } else {
                log.error("Backend responded with error: [{}]", code);
                return HttpResponse.<R>builder()
                        .errorMessage("It seems that the server could not process your request." + "\n" + IOUtils.toString(conn.getErrorStream(), StandardCharsets.UTF_8))
                        .build();
            }


        } catch (Exception e) {
            log.error("JSON POST failed", e);
            return HttpResponse.<R>builder()
                    .errorMessage("An unexpected error occured, sorry for the inconvenience")
                    .build();
        }
    }

    public void delete(String endpoint) {
        delete(DeleteRequest.builder()
                .endpoint(endpoint)
                .build());
    }

    public <T> void delete(DeleteRequest<T> request) {
        try {
            URL url = new URL(getBaseUrl() + request.getEndpoint());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(HttpMethod.DELETE.name());
            conn.setDoOutput(true);
            conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE);

            if (sessionWrapper.isAuthenticated()) {
                conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + sessionWrapper.getToken());
            }

            T body = request.getBody();
            if (body != null) {
                String json = objectMapper.writeValueAsString(body);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = json.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }

            int code = conn.getResponseCode();

            if (code >= 200 && code < 300) {
                log.info("Success");
            } else {
                log.error("Backend responded with error: [{}]", code);
            }


        } catch (Exception e) {
            log.error("JSON POST failed", e);
        }
    }

    @Builder
    @Data
    public static final class PostRequest<T, R> {
        @NotNull
        private final String endpoint;

        @NotNull
        private final T body;

        @Nullable
        private final Class<R> returnType;
    }

    @Builder
    @Data
    public static final class GetRequest<R> {
        @Nullable
        private final String endpoint;

        @Nullable
        private final String fullEndpoint;

        @Nullable
        private final Class<R> returnType;

        @Nullable
        private final String token;
    }

    @Builder
    @Data
    public static final class DeleteRequest<T> {
        @NotNull
        private final String endpoint;

        @Nullable
        private final T body;
    }

    @Builder
    @Data
    public static final class HttpResponse<R> {
        @Nullable
        private R data;

        @Nullable
        private String errorMessage;

        public boolean hasError() {
            return StringUtils.isNoneEmpty(errorMessage);
        }
    }
}
