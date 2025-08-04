package lu.pokevax.test.integration;

import lombok.SneakyThrows;
import lu.pokevax.business.notification.VaccineNotificationResponse;
import lu.pokevax.business.notification.VaccineNotificationResponseWrapper;
import lu.pokevax.business.user.requests.CreateUserRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VaccineNotificationSpringBootTest extends BaseSpringBootTest {

    @SneakyThrows
    @Test
    void asset_notifications_are_created() {
        // PREPARE
        // using a "new-born" allows to create all notifications
        CreateUserRequest request = CreateUserRequest.builder()
                .birthDate(LocalDate.now().minusDays(1))
                .email("mail@mail.fr")
                .name("name")
                .surname("surname")
                .password("password")
                .build();

        // EXECUTE
        CreateUserSummary createdUser = createUser(request);
        Integer userId = createdUser.getUserId();

        // CHECK
        MvcResult result = mockMvc.perform(buildAuthorizedGetRequest(VACCINE_NOTIFICATIONS_URI, userId))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        VaccineNotificationResponseWrapper actual = readJson(result.getResponse(), VaccineNotificationResponseWrapper.class);

        Assertions.assertThat(actual.getContent())
                .isNotEmpty()
                .extracting(VaccineNotificationResponse::getDeadline)
                .isSorted();
    }
}
