package lu.vaccineo.technical.persistence;

import lu.vaccineo.test.unit.BaseUnitTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateAttributeConverterTest extends BaseUnitTest {

    private final LocalDateAttributeConverter victim = new LocalDateAttributeConverter();

    @Test
    void convertToDatabaseColumn_shouldReturnEpochDay_whenLocalDateIsNotNull() {
        // PREPARE
        LocalDate date = LocalDate.of(2020, 1, 15);
        long expectedEpoch = date.toEpochDay();

        // EXECUTE
        Long result = victim.convertToDatabaseColumn(date);

        // CHECK
        assertThat(result).isEqualTo(expectedEpoch);
    }

    @Test
    void convertToDatabaseColumn_shouldReturnNull_whenLocalDateIsNull() {
        // PREPARE
        LocalDate date = null;

        // EXECUTE
        Long result = victim.convertToDatabaseColumn(date);

        // CHECK
        assertThat(result).isNull();
    }

    @Test
    void convertToEntityAttribute_shouldReturnLocalDate_whenEpochDayIsNotNull() {
        // PREPARE
        long epochDay = 18262L;
        LocalDate expectedDate = LocalDate.ofEpochDay(epochDay);

        // EXECUTE
        LocalDate result = victim.convertToEntityAttribute(epochDay);

        // CHECK
        assertThat(result).isEqualTo(expectedDate);
    }

    @Test
    void convertToEntityAttribute_shouldReturnNull_whenEpochDayIsNull() {
        // PREPARE
        Long dbData = null;

        // EXECUTE
        LocalDate result = victim.convertToEntityAttribute(dbData);

        // CHECK
        assertThat(result).isNull();
    }
}
