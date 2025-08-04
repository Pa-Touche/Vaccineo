package lu.pokevax.technical.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Enriches the request with the
 *
 * @param <T> request type
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserIdWrapper<T> {
    private Integer userId;
    private T request;
}
