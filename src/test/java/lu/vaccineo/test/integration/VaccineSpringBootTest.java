package lu.vaccineo.test.integration;

import lu.vaccineo.business.vaccine.administered.VaccineSortableField;
import lu.vaccineo.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import lu.vaccineo.business.vaccine.administered.requests.SearchVaccineCriteria;
import lu.vaccineo.business.vaccine.administered.requests.SearchVaccineRequest;
import lu.vaccineo.business.vaccine.administered.requests.SortRequest;
import lu.vaccineo.business.vaccine.administered.responses.AdministeredVaccineResponse;
import lu.vaccineo.business.vaccine.administered.responses.AdministeredVaccineResponseWrapper;
import lu.vaccineo.test.VaccineName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VaccineSpringBootTest extends BaseSpringBootTest {


    @Test
    public void add_vaccine_exact_search() throws Exception {
        // PREPARE
        CreateUserSummary createdUser = createRandomUser();

        postVaccine(createdUser, CreateAdministeredVaccineRequest.builder()
                .vaccineName(VaccineName.MENINGOCOQUES_ACWY.getDescription())
                .comment("Comment A")
                .doseNumber(1)
                .administrationDate(LocalDate.now().minusDays(5))
                .build());

        // EXECUTE & CHECK
        SearchVaccineCriteria searchCriteria = SearchVaccineCriteria.builder()
                .vaccineName(VaccineName.MENINGOCOQUES_ACWY.getDescription())
                .doseNumber(1)
                .administrationDate(LocalDate.now().minusDays(5))
                .build();
        MvcResult result = mockMvc.perform(buildAuthorizedPostRequest(VACCINE_URI + "/search", createdUser.getUserId())
                        .content(toJson(SearchVaccineRequest.builder()
                                .criteria(searchCriteria)
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        AdministeredVaccineResponseWrapper actual = readJson(result.getResponse(), AdministeredVaccineResponseWrapper.class);

        Assertions.assertThat(actual.getContent())
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .ignoringFields("id", "comment")
                .isEqualTo(searchCriteria);
    }

    @Test
    public void add_vaccine_no_parameter() throws Exception {
        // PREPARE
        CreateUserSummary createdUser = createRandomUser();

        LocalDate administrationDate = LocalDate.now().minusDays(5);
        postVaccine(createdUser, CreateAdministeredVaccineRequest.builder()
                .vaccineName(VaccineName.PNEUMOCOQUES.getDescription())
                .doseNumber(1)
                .administrationDate(administrationDate)
                .build());

        postVaccine(createdUser, CreateAdministeredVaccineRequest.builder()
                .vaccineName(VaccineName.COMBINED_VACCINE_RORV.getDescription())
                .doseNumber(2)
                .administrationDate(administrationDate.minusDays(2))
                .build());

        // EXECUTE & CHECK
        MvcResult result = mockMvc.perform(buildAuthorizedPostRequest(VACCINE_URI + "/search", createdUser.getUserId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        AdministeredVaccineResponseWrapper actual = readJson(result.getResponse(), AdministeredVaccineResponseWrapper.class);

        Assertions.assertThat(actual.getContent())
                .hasSize(2)
                .extracting(AdministeredVaccineResponse::getAdministrationDate)
                .isSortedAccordingTo(Comparator.reverseOrder());
    }


    @Test
    public void add_vaccine_custom_criteria() throws Exception {
        // PREPARE
        CreateUserSummary createdUser = createRandomUser();

        LocalDate administrationDate = LocalDate.now().minusDays(5);
        postVaccine(createdUser, CreateAdministeredVaccineRequest.builder()
                .vaccineName(VaccineName.RSV.getDescription())
                .comment("no comment")
                .doseNumber(1)
                .administrationDate(administrationDate)
                .build());

        postVaccine(createdUser, CreateAdministeredVaccineRequest.builder()
                .vaccineName(VaccineName.ROTAVIRUS.getDescription())
                .doseNumber(1)
                .administrationDate(administrationDate.minusDays(2))
                .build());

        postVaccine(createdUser, CreateAdministeredVaccineRequest.builder()
                .vaccineName(VaccineName.MENINGOCOQUE.getDescription())
                .doseNumber(1)
                .administrationDate(administrationDate.minusDays(4))
                .build());

        // must be ignored
        postVaccine(createdUser, CreateAdministeredVaccineRequest.builder()
                .vaccineName(VaccineName.RSV.getDescription())
                .doseNumber(2)
                .administrationDate(administrationDate)
                .build());

        // EXECUTE & CHECK
        MvcResult result = mockMvc.perform(buildAuthorizedPostRequest(VACCINE_URI + "/search", createdUser.getUserId())
                        .content(toJson(SearchVaccineRequest.builder()
                                .criteria(SearchVaccineCriteria.builder()
                                        .doseNumber(1)
                                        .build())
                                .sort(SortRequest.builder()
                                        .fields(Arrays.asList(VaccineSortableField.VACCINE_NAME, VaccineSortableField.ADMINISTRATION_DATE))
                                        .direction(Sort.Direction.ASC)
                                        .build())
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        AdministeredVaccineResponseWrapper actual = readJson(result.getResponse(), AdministeredVaccineResponseWrapper.class);

        Assertions.assertThat(actual.getContent())
                .hasSize(3)
                .extracting(AdministeredVaccineResponse::getVaccineName)
                .isSorted();
    }

    private void postVaccine(CreateUserSummary createdUser, CreateAdministeredVaccineRequest request) throws Exception {
        mockMvc.perform(buildAuthorizedPostRequest(VACCINE_URI, createdUser.getUserId())
                        .content(toJson(request)))
                .andExpect(status().is2xxSuccessful());
    }

}
