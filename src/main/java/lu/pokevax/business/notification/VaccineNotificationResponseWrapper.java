package lu.pokevax.business.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VaccineNotificationResponseWrapper {
    private List<VaccineNotificationResponse> content;
}
