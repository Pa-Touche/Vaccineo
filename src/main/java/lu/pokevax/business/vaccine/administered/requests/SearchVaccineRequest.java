package lu.pokevax.business.vaccine.administered.requests;


import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@Builder
public class SearchVaccineRequest {

    @Nullable
    private SortRequest sort;

    @Nullable
    private SearchVaccineCriteria criteria;

}
