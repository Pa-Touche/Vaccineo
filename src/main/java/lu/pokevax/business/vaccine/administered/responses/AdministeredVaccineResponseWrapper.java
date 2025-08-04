package lu.pokevax.business.vaccine.administered.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministeredVaccineResponseWrapper {
    private List<AdministeredVaccineResponse> content;
}
