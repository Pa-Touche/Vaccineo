package lu.pokevax.business.vaccine.administered.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchVaccineRequest {

    @Nullable
    private SortRequest sort;

    @Nullable
    private SearchVaccineCriteria criteria;

}
