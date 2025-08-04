package lu.pokevax.business.vaccine.administered;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministeredVaccineRepository extends JpaRepository<AdministeredVaccineEntity, Integer>, JpaSpecificationExecutor<AdministeredVaccineEntity> {

}
