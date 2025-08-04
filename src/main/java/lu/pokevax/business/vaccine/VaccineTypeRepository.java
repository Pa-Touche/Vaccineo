package lu.pokevax.business.vaccine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaccineTypeRepository extends JpaRepository<VaccineTypeEntity, Integer> {

    boolean existsByName(String name);

    Optional<VaccineTypeEntity> findByName(String name);
}
