package user_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import user_management.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
