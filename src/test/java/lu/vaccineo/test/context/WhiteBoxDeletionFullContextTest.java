package lu.vaccineo.test.context;

import lombok.SneakyThrows;
import lu.vaccineo.business.notification.VaccineNotificationRepository;
import lu.vaccineo.business.notification.VaccineNotificationResponseWrapper;
import lu.vaccineo.business.user.UserPasswordRepository;
import lu.vaccineo.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import lu.vaccineo.business.vaccine.administered.responses.AdministeredVaccineResponseWrapper;
import lu.vaccineo.test.VaccineName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Meant to check if all {@link lu.vaccineo.business.user.UserEntity} "attached" entities are properly deleted.
 * <p>
 * Why ? because without full access to Spring beans those tests couldn't be automated.
 * <p>
 * https://en.wikipedia.org/wiki/White-box_testing.
 */
public class WhiteBoxDeletionFullContextTest extends BaseFullContextTest {

    @Autowired
    private UserPasswordRepository userPasswordRepository;

    @Autowired
    private VaccineNotificationRepository vaccineNotificationRepository;

    @SneakyThrows
    @Test
    void verify_all_entities_are_deleted() {
        // PREPARE
        CreateUserSummary createdUser = createRandomUser();

        Integer userId = createdUser.getUserId();
        buildAuthorizedPostRequest(VACCINE_URI, userId);

        mockMvc.perform(buildAuthorizedPostRequest(VACCINE_URI, createdUser.getUserId())
                        .content(toJson(CreateAdministeredVaccineRequest.builder()
                                .vaccineName(VaccineName.MENINGOCOQUES_ACWY.getDescription())
                                .doseNumber(1)
                                .administrationDate(LocalDate.now().minusDays(5))
                                .build())))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(buildAuthorizedPostRequest(VACCINE_URI, createdUser.getUserId())
                        .content(toJson(CreateAdministeredVaccineRequest.builder()
                                .vaccineName(VaccineName.COMBINED_VACCINE_D_T_AP_HIB_IPV_HEP_B.getDescription())
                                .doseNumber(3)
                                .administrationDate(LocalDate.now().minusDays(5))
                                .build())))
                .andExpect(status().is2xxSuccessful());

        // EXECUTE
        mockMvc.perform(buildAuthorizedDeleteRequest(uriWithIdentifier(USER_URI, userId), userId))
                .andExpect(status().is2xxSuccessful());

        // CHECK
        MvcResult vaccinesSearchResult = mockMvc.perform(buildAuthorizedPostRequest(VACCINE_URI + "/search", createdUser.getUserId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Assertions.assertThat(readJson(vaccinesSearchResult.getResponse(), AdministeredVaccineResponseWrapper.class).getContent())
                .isEmpty();

        // Notifications

        MvcResult vaccineNotificationsResult = mockMvc.perform(buildAuthorizedGetRequest(VACCINE_NOTIFICATIONS_URI, createdUser.getUserId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Assertions.assertThat(readJson(vaccineNotificationsResult.getResponse(), VaccineNotificationResponseWrapper.class).getContent())
                .isEmpty();


        // actual whitebox checks: creates a strong dependency
        Assertions.assertThat(userPasswordRepository.findByUserEmail(createdUser.getEmail()))
                .isEmpty();
    }
}
