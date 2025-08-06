package lu.vaccineo.business.vaccine.administered;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Like this exposes internal persistence fields to client, to avoid this create another class that uses this as reference.
 */
@Getter
@RequiredArgsConstructor
public enum VaccineSortableField {
    VACCINE_NAME("vaccineType.name"),
    COMMENT("comment"),
    DOSE_NUMBER("doseNumber"),
    ADMINISTRATION_DATE("administrationDate");

    @NotNull
    private final String fieldName;

}
