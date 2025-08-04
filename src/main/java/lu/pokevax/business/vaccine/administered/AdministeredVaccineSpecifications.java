package lu.pokevax.business.vaccine.administered;

import lu.pokevax.business.user.UserEntity;
import lu.pokevax.business.vaccine.VaccineTypeEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.time.LocalDate;

public class AdministeredVaccineSpecifications {

    public static Specification<AdministeredVaccineEntity> hasUserId(Integer userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                throw new IllegalStateException("Look like no user id was provided, this cannot occur as a user should only see it's own vaccines");
            }
            Join<AdministeredVaccineEntity, UserEntity> userJoin = root.join("user");
            return criteriaBuilder.equal(userJoin.get("id"), userId);
        };
    }

    public static Specification<AdministeredVaccineEntity> hasVaccineName(String vaccineName) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isEmpty(vaccineName)) {
                return criteriaBuilder.conjunction();
            }
            Join<AdministeredVaccineEntity, VaccineTypeEntity> vaccineTypeJoin = root.join("vaccineType");
            return criteriaBuilder.equal(vaccineTypeJoin.get("name"), vaccineName);
        };
    }

    public static Specification<AdministeredVaccineEntity> hasAdministrationDate(LocalDate administrationDate) {
        return (root, query, criteriaBuilder) -> {
            if (administrationDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("administrationDate"), administrationDate);
        };
    }

    public static Specification<AdministeredVaccineEntity> hasDoseNumber(Integer doseNumber) {
        return (root, query, criteriaBuilder) -> {
            if (doseNumber == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("doseNumber"), doseNumber);
        };
    }

    public static Specification<AdministeredVaccineEntity> hasCommentContaining(String comment) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isEmpty(comment)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("comment"), "%" + comment + "%");
        };
    }
}