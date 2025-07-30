package lu.pokevax.technical.dialect;

public interface EntityContract {

    /**
     * Why not {@link Long} ? was convenient with SQL-lite to use AUTOINCREMENT feature.
     *
     * @return db identifier of the entity.
     */
    Integer getId();
}
