package lu.pokevax.business.vaccine.administered;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdministeredVaccineService {
    private final AdministeredVaccineRepository repository;

    public Long create(CreateAdministeredVaccineRequest request) {
        //repository.
        return null;
    }
}
