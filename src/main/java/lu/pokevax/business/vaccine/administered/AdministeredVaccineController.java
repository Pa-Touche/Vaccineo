package lu.pokevax.business.vaccine.administered;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/vaccines")
@RequiredArgsConstructor
@Slf4j
public class AdministeredVaccineController {


    private final AdministeredVaccineService service;


    @PostMapping
    public void create(@RequestBody CreateAdministeredVaccineRequest request) {
        log.debug("create [{}]", request);
        service.create(request);
    }
}
