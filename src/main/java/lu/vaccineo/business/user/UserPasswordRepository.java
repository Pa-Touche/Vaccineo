package lu.vaccineo.business.user;

import lu.vaccineo.business.user.login.LoginRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPasswordRepository extends JpaRepository<UserPasswordEntity, LoginRequest> {

    Optional<UserPasswordEntity> findByUserEmail(String email);
}
