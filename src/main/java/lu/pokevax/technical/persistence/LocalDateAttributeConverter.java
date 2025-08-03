package lu.pokevax.technical.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.util.Optional;

@Converter
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Long> {
    @Override
    public Long convertToDatabaseColumn(LocalDate attribute) {
        return Optional.ofNullable(attribute)
                .map(LocalDate::toEpochDay)
                .orElse(null);
    }

    @Override
    public LocalDate convertToEntityAttribute(Long dbData) {
        return Optional.ofNullable(dbData)
                .map(LocalDate::ofEpochDay)
                .orElse(null);
    }
}
