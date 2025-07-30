package lu.pokevax.technical.utils;

import lu.pokevax.technical.dialect.EntityContract;

/**
 * There is no silver-bullet for implementing {@link Object#equals(Object)} and {@link Object#hashCode()} within @{@link javax.persistence.Entity}.
 * This approach is heavily inspired from: https://vladmihalcea.com/hibernate-facts-equals-and-hashcode/
 */
public abstract class BaseEntity implements EntityContract {

    @Override
    public boolean equals(Object o) {
        return EntityUtils.entityEquals(this, o);
    }

    /**
     * This deteriorates performance for 'hash'-collections {@link java.util.HashSet} but is accepted.
     *
     * @return same hashcode for all class instances.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
