package lu.pokevax.technical.utils;

import lu.pokevax.technical.dialect.EntityContract;

public abstract class BaseEntity implements EntityContract {

    @Override
    public boolean equals(Object o) {
        return EntityUtils.entityEquals(this, o);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
