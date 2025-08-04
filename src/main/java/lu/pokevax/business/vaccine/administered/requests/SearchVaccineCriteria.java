package lu.pokevax.business.vaccine.administered.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lu.pokevax.business.vaccine.administered.validation.ValidVaccineName;
import org.springframework.lang.Nullable;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchVaccineCriteria {

    @Nullable
    @ValidVaccineName
    private String vaccineName;

    @PastOrPresent
    @Nullable
    private LocalDate administrationDate;

    // Not indexed, will be slow.
    @Positive
    @Nullable
    private Integer doseNumber;
}
