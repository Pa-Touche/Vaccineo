package lu.pokevax.business.vaccine.administered.requests;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder
public class SearchVaccineCriteria {

    @Nullable
    private String vaccineName;

    @PastOrPresent
    @Nullable
    private LocalDate administrationDate;

    @Positive
    @Nullable
    private Integer doseNumber;

    @Nullable
    private String comment;
}
