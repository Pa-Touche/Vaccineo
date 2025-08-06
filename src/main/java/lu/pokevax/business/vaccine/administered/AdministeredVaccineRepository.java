package lu.pokevax.business.vaccine.administered;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministeredVaccineRepository extends JpaRepository<AdministeredVaccineEntity, Integer>, JpaSpecificationExecutor<AdministeredVaccineEntity> {

    @EntityGraph(attributePaths = {
            "user",
            "vaccineType"
    })
    List<AdministeredVaccineEntity> findAll(@Nullable Specification<AdministeredVaccineEntity> spec, Sort sort);
}
