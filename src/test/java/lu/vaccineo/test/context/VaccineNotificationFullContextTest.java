package lu.vaccineo.test.context;

import lombok.SneakyThrows;
import lu.vaccineo.business.notification.VaccineNotificationResponse;
import lu.vaccineo.business.notification.VaccineNotificationResponseWrapper;
import lu.vaccineo.business.user.requests.CreateUserRequest;
import lu.vaccineo.test.RandomDataUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VaccineNotificationFullContextTest extends BaseFullContextTest {

    @SneakyThrows
    @Test
    void asset_notifications_are_created() {
        // PREPARE
        // using a "new-born" allows to create all notifications
        CreateUserRequest request = CreateUserRequest.builder()
                .birthDate(LocalDate.now().minusDays(1))
                .email(RandomDataUtils.emailAddress())
                .name(RandomDataUtils.emailAddress())
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
