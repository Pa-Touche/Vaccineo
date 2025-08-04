package lu.pokevax.business.vaccine.administered;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Like this exposes internal persistence fields to client, to avoid this create another class that uses this as reference.
 */
@Getter
@RequiredArgsConstructor
public enum VaccineSortableField {
    VACCINE_NAME("vaccineType.name"),
    ADMINISTRATION_DATE("administrationDate");

    private final String fieldName;
}
