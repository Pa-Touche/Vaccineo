package lu.pokevax.test.integration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VaccineName {
    RSV("RSV (virus respiratoire syncytial)"),
    COMBINED_VACCINE_D_T_AP_HIB_IPV_HEP_B("Vaccin combiné (D, T, aP, Hib, IPV, Hep B)"),
    ROTAVIRUS("Rotavirus"),
    PNEUMOCOQUES("Pneumocoques"),
    MENINGOCOQUE("Méningocoque B"),
    COMBINED_VACCINE_RORV("Vaccin combiné (RORV)"),
    MENINGOCOQUES_ACWY("Méningocoques ACWY");

    private final String description;


}
