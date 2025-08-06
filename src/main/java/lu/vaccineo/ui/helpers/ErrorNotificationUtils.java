package lu.vaccineo.ui.helpers;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class ErrorNotificationUtils {


    /**
     * If a mapping between technical (backend) field name and displayed field name is passed, the error could be shown in a nicer manner.
     * @param validationError errors determined during Java Bean validation.
     * @return 'humand'-readable string.
     */
    public static String buildReadableValidationError(Map<String, String> validationError) {
        return validationError.entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
    }
}
