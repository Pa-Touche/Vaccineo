package lu.vaccineo.business.vaccine.administered.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineTypeResponseWrapper {
    private List<String> content;
}
