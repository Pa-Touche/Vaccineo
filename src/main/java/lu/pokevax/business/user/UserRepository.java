package lu.pokevax.business.user;

import lu.pokevax.business.user.login.LoginRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, LoginRequest> {
}
