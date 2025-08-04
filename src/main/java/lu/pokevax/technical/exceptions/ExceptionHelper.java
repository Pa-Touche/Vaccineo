package lu.pokevax.technical.exceptions;

import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import org.sqlite.SQLiteException;

import java.util.Optional;

@Component
public class ExceptionHelper {

    public Optional<SQLiteException> extractSqlLiteException(@Nullable JpaSystemException e) {
        return Optional.ofNullable(e)
                .map(Throwable::getCause)
                .map(Throwable::getCause)
                .filter(SQLiteException.class::isInstance)
                .map(SQLiteException.class::cast);
    }
}
