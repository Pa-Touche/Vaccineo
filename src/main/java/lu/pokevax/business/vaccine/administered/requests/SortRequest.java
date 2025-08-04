package lu.pokevax.business.vaccine.administered.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lu.pokevax.business.vaccine.administered.VaccineSortableField;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SortRequest {
    @Nullable
    private Sort.Direction direction;

    /**
     * Enums should be avoided in response as they can induce breaking changes
     * (for example for Java Client with enum, with default fallback strategy), but here it's acceptable as request param.
     */
    @Nullable
    private List<VaccineSortableField> fields;

}
