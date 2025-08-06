package lu.vaccineo.business.vaccine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.vaccineo.business.vaccine.administered.AdministeredVaccineService;
import lu.vaccineo.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import lu.vaccineo.business.vaccine.administered.requests.SearchVaccineRequest;
import lu.vaccineo.business.vaccine.administered.responses.AdministeredVaccineResponseWrapper;
import lu.vaccineo.business.vaccine.administered.responses.VaccineTypeResponseWrapper;
import lu.vaccineo.technical.ValidatedRestController;
import lu.vaccineo.technical.web.WebTokenExtractor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@ValidatedRestController
@RequestMapping(value = VaccineController.URI)
@RequiredArgsConstructor
@Slf4j
public class VaccineController {

    public static final String URI = "/api/vaccines";

    private final AdministeredVaccineService service;
    private final WebTokenExtractor webTokenExtractor;


    @GetMapping("/types")
    public VaccineTypeResponseWrapper retrieveVaccineTypes() {
        return VaccineTypeResponseWrapper.builder()
                .content(service.retrieveVaccineTypes())
                .build();
    }

    @PostMapping
    public Integer create(HttpServletRequest httpServletRequest,
                          @Valid @RequestBody CreateAdministeredVaccineRequest request) {
        log.debug("create [{}]", request);
        return service.create(webTokenExtractor.enrichRequestWithUserID(httpServletRequest, request));
    }

    @PostMapping("/search")
    public AdministeredVaccineResponseWrapper search(HttpServletRequest httpServletRequest,
                                                     @Valid @RequestBody(required = false) SearchVaccineRequest request) {
        log.debug("search [{}]", request);
        return AdministeredVaccineResponseWrapper.builder()
                .content(service.search(webTokenExtractor.enrichRequestWithUserID(httpServletRequest, request)))
                .build();
    }
}
