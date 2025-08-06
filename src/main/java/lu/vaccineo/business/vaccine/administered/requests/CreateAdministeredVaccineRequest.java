package lu.vaccineo.business.vaccine.administered.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lu.vaccineo.business.vaccine.administered.validation.ValidVaccineName;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdministeredVaccineRequest {
    @NotEmpty
    @ValidVaccineName
    private String vaccineName;

    @PastOrPresent
    @NotNull
    private LocalDate administrationDate;

    @NotNull
    @Positive
    private Integer doseNumber;

    @Nullable
    private String comment;
}
