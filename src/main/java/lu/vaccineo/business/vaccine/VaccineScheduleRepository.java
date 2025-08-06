package lu.vaccineo.business.vaccine;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineScheduleRepository extends JpaRepository<VaccineScheduleEntity, Integer> {
}
