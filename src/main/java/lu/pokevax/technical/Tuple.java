package lu.pokevax.technical;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@RequiredArgsConstructor
public class Tuple<F, S> {
    private final F first;
    private final S second;
}
