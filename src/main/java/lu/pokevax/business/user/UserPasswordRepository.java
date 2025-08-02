package lu.pokevax.business.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPasswordRepository extends JpaRepository<UserPasswordEntity, LoginRequest> {

    Optional<UserPasswordEntity> findByUserEmail(String email);
}
