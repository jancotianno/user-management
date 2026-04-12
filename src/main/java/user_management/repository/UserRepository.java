package user_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import user_management.entity.User;
import user_management.enumeration.UserStatus;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByCodiceFiscale(String codiceFiscale);

    Optional<User> findByEmail(String email);

    Page<User> findAllByStatusNot(UserStatus status, Pageable pageable);

    Optional<User> findByCodiceFiscaleAndIdNot(String codiceFiscale, Long id);

    Optional<User> findByUsername(String username);

}
