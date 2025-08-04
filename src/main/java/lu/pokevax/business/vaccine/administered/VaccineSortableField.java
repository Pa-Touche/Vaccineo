package lu.pokevax.business.vaccine.administered;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Exposes internal storage to client.
 */
@Getter
@RequiredArgsConstructor
public enum VaccineSortableField {
    VACCINE_NAME("vaccineType.name"),
    ADMINISTRATION_DATE("administrationDate");

    private final String fieldName;
}
