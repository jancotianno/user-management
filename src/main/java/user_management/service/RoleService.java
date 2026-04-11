package user_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user_management.entity.Role;
import user_management.repository.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role createRole(String name) {

        if (roleRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Role già esistente");
        }

        return roleRepository.save(
                Role.builder().name(name).build()
        );
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
