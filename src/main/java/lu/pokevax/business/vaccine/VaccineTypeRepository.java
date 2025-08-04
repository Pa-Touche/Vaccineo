package lu.pokevax.business.vaccine;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VaccineTypeRepository extends JpaRepository<VaccineTypeEntity, Integer> {

    Optional<VaccineTypeEntity> findByName(String name);
}
