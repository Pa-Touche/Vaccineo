package lu.pokevax.technical.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.technical.dialect.EntityContract;
import org.hibernate.Hibernate;

import java.util.Objects;

/**
 * Opinionated {@link Object#equals(Object)} implementation for {@link javax.persistence.Entity} classes.
 * <p>
 * This approach is heavily inspired from: https://vladmihalcea.com/hibernate-facts-equals-and-hashcode/
 */
@UtilityClass
@Slf4j
public class EntityUtils {

    /**
     * Avoids Entityduplication for equals method.
     *
     * @param that 'this' reference
     * @param o    object candidate for equals check.
     * @param <T>  type of current object.
     * @return true if both objects are equals, else false.
     */
    public static <T extends EntityContract> boolean entityEquals(T that, Object o) {
        log.trace("equals: [{}] and [{}]", that, o);
        if (that == o) {
            return true;
        }

        if (o == null || Hibernate.getClass(that) != Hibernate.getClass(o)) {
            return false;
        }

        return Objects.equals(that.getId(), ((T) o).getId());
    }
}
