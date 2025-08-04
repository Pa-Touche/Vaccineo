package lu.pokevax.test.integration;

import lu.pokevax.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VaccineSpringBootTest extends BaseSpringBootTest {

    @Test
    public void valid_bearer_token() throws Exception {
        // PREPARE
        CreateUserSummary createdUser = createRandomUser();

        // EXECUTE & CHECK
        mockMvc.perform(buildAuthorizedPostRequest(VACCINE_URI, createdUser.getUserId())
                        .content(toJson(CreateAdministeredVaccineRequest.builder()
                                .vaccineName(VaccineName.MENINGOCOQUES_ACWY.getDescription())
                                .comment("Some random comment")
                                .doseNumber(1)
                                .administrationDate(LocalDate.now().minusDays(5))
                                .build())))
                .andExpect(status().is2xxSuccessful());

    }


}
