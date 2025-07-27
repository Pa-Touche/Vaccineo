package lu.pokevax;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VaccineController {


    private final VaccineRepository vaccineRepository;

    public VaccineController(VaccineRepository vaccineRepository) {
        this.vaccineRepository = vaccineRepository;
    }

    @GetMapping("/vaccines")
    public List<VaccineEntity> helloWorld() {
        return vaccineRepository.findAll();
    }

    @PostMapping("/vaccines")
    public void create(@RequestBody VaccineEntity vaccineEntity) {
        vaccineRepository.save(vaccineEntity);
    }

    @DeleteMapping("/vaccines/{id}")
    public void delete(@PathVariable long id) {
        vaccineRepository.deleteById(id);
    }
}
