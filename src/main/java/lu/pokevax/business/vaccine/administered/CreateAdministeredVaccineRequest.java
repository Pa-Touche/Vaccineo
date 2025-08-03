package lu.pokevax.business.vaccine.administered;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.OffsetDateTime;

@Data
@Builder
public class CreateAdministeredVaccineRequest {
    @NotNull
    @NotEmpty
    private final String vaccineName;

    @PastOrPresent
    private OffsetDateTime administrationDateTime;

    @NotNull
    @Positive
    private Integer doseNumber;

    @Nullable
    private String comment;


    /**
     * Using the userId is convenient, but could be extracted
     */
    @NotNull
    private Integer userId;
}
