package lu.pokevax.business.vaccine.administered;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lu.pokevax.business.vaccine.administered.requests.CreateAdministeredVaccineRequest;
import lu.pokevax.business.vaccine.administered.requests.SearchVaccineRequest;
import lu.pokevax.business.vaccine.administered.responses.AdministeredVaccineResponse;
import lu.pokevax.technical.ValidatedRestController;
import lu.pokevax.technical.web.WebTokenExtractor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@ValidatedRestController
@RequestMapping(value = AdministeredVaccineController.URI)
@RequiredArgsConstructor
@Slf4j
public class AdministeredVaccineController {

    public static final String URI = "/vaccines";

    private final AdministeredVaccineService service;
    private final WebTokenExtractor webTokenExtractor;


    @PostMapping
    public Integer create(HttpServletRequest httpServletRequest,
                          @Valid @RequestBody CreateAdministeredVaccineRequest request) {
        log.debug("create [{}]", request);
        return service.create(webTokenExtractor.enrichRequestWithUserID(httpServletRequest, request));
    }

    @PostMapping("/search")
    public List<AdministeredVaccineResponse> search(HttpServletRequest httpServletRequest,
                                                    @Valid @RequestBody SearchVaccineRequest request) {
        log.debug("search [{}]", request);
        return service.search(webTokenExtractor.enrichRequestWithUserID(httpServletRequest, request));
    }
}
