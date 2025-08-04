package lu.pokevax.business.vaccine.administered.requests;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@Builder
public class SortRequest {
    @Nullable
    private Sort.Direction direction;

    @Nullable
    private List<String> fields;

}
