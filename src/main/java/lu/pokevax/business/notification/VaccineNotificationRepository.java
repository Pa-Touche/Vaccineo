package lu.pokevax.business.notification;

import lu.pokevax.business.notification.projections.VaccineNotificationDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccineNotificationRepository extends JpaRepository<VaccineNotificationEntity, Integer> {

    @Query("SELECT new lu.pokevax.business.notification.projections.VaccineNotificationDto(vn.deadline, vs.doseNumber, vt.name) " +
            "FROM notification_vaccine vn " +
            "JOIN vn.vaccineSchedule vs " +
            "JOIN vs.vaccineType vt " +
            "WHERE vn.user.id = :userId")
    List<VaccineNotificationDto> findAllNotifications(@Param("userId") Integer userId, Sort sort);

    @Modifying
    @Query("DELETE FROM notification_vaccine WHERE deadline < :today")
    int deleteAllOlderThan(@Param("today") LocalDate today);
}
