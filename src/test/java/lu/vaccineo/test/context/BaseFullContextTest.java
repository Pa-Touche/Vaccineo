package lu.vaccineo.test.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lu.vaccineo.business.notification.VaccineNotificationController;
import lu.vaccineo.business.user.UserController;
import lu.vaccineo.business.user.login.LoginController;
import lu.vaccineo.business.user.login.LoginRequest;
import lu.vaccineo.business.user.login.LoginResponse;
import lu.vaccineo.business.user.requests.CreateUserRequest;
import lu.vaccineo.business.vaccine.VaccineController;
import lu.vaccineo.technical.security.JwtHelper;
import lu.vaccineo.test.RandomDataUtils;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Allows to perform some kind of white-box testing: as the spring boot context is accessible.
 * Here it's meant to be used to check controller flows.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
@Tag("springBoot") // could be used for running some tests separately
@Import(value = BaseFullContextTest.CommonBaseConfiguration.class)
public class BaseFullContextTest {

    protected String VACCINE_URI = VaccineController.URI;
    protected String VACCINE_NOTIFICATIONS_URI = VaccineNotificationController.URI;
    protected String USER_URI = UserController.URI;
    protected String LOGIN_URI = LoginController.URI;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JwtHelper jwtHelper;

    @Autowired
    private ObjectMapper objectMapper;


    @SneakyThrows
    public CreateUserSummary createRandomUser() {
        String email = RandomDataUtils.emailAddress();
        String password = RandomDataUtils.password();
        CreateUserRequest request = CreateUserRequest.builder()
                .birthDate(RandomDataUtils.birthDate())
                .email(email)
                .name(RandomDataUtils.lastName())
                .surname(RandomDataUtils.surname())
                .password(password)
                .build();

        return createUser(request);
    }

    @SneakyThrows
    public CreateUserSummary createUser(CreateUserRequest request) {
        MvcResult result = mockMvc.perform(post(USER_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Integer createdUserId = Integer.valueOf(result.getResponse().getContentAsString());

        return CreateUserSummary.builder()
                .userId(createdUserId)
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    @SneakyThrows
    public LoginResponse login(CreateUserSummary createUserSummary) {
        return login(createUserSummary.getEmail(), createUserSummary.getPassword());
    }

    @SneakyThrows
    public LoginResponse login(String email, String password) {
        MvcResult result = mockMvc.perform(post(LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(LoginRequest.builder()
                                .email(email)
                                .password(password)
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        return readJson(result.getResponse(), LoginResponse.class);
    }

    @SneakyThrows
    protected String toJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }


    @SneakyThrows
    protected <T> T readJson(MockHttpServletResponse response, Class<T> target) {
        return objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), target);
    }

    protected MockHttpServletRequestBuilder buildAuthorizedPostRequest(String urlTemplate, Integer userId) {
        String validToken = jwtHelper.generateToken(userId);

        return post(urlTemplate)
                .header("Authorization", "Bearer " + validToken)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON);
    }

    protected MockHttpServletRequestBuilder buildAuthorizedDeleteRequest(String urlTemplate, Integer userId) {
        String validToken = jwtHelper.generateToken(userId);

        return delete(urlTemplate)
                .header("Authorization", "Bearer " + validToken)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON);
    }

    protected MockHttpServletRequestBuilder buildAuthorizedGetRequest(String urlTemplate, Integer userId) {
        String validToken = jwtHelper.generateToken(userId);

        return get(urlTemplate)
                .header("Authorization", "Bearer " + validToken)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON);
    }

    protected String uriWithIdentifier(String uri, Object value) {
        return String.format("%s/%s", uri, value);
    }

    @TestConfiguration
    @Slf4j
    public static class CommonBaseConfiguration {
        @Bean
        public FlywayMigrationStrategy clean() {
            return flyway -> {
                flyway.clean();
                flyway.migrate();
            };
        }
    }

    @Builder
    @Data
    public static final class CreateUserSummary {
        @NotNull
        private Integer userId;

        @NotNull
        private String email;

        @NotNull
        private String password;
    }

}
