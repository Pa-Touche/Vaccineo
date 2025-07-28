package lu.pokevax.business.vaccine;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VaccineController {


    private final VaccineRepository vaccineRepository;

    @GetMapping("/vaccines")
    public List<VaccineTypeEntity> helloWorld() {
        return vaccineRepository.findAll();
    }

    @PostMapping("/vaccines")
    public void create(@RequestBody VaccineTypeEntity vaccineTypeEntity) {
        vaccineRepository.save(vaccineTypeEntity);
    }

    @DeleteMapping("/vaccines/{id}")
    public void delete(@PathVariable long id) {
        vaccineRepository.deleteById(id);
    }
}
