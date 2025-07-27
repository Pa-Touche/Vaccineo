package lu.pokevax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineRepository extends JpaRepository<VaccineEntity, Long> {

    @Override
    @Query(value = "DELETE FROM vaccine", nativeQuery = true)
    void deleteAll();
}
