package lu.pokevax.business.vaccine.administered.responses;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder
public class AdministeredVaccineResponse {

    @NotNull
    private Integer id;

    @NotNull
    @NotEmpty
    private String vaccineName;

    @PastOrPresent
    private LocalDate administrationDate;

    @NotNull
    @Positive
    private Integer doseNumber;

    @Nullable
    private String comment;
}
