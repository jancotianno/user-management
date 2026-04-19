package user_management.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import user_management.entity.Role;
import user_management.entity.User;
import user_management.enumeration.UserStatus;
import user_management.repository.RoleRepository;
import user_management.repository.UserRepository;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class UserBootstrap {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {

        if (userRepository.existsByUsername("admin")) {
            return;
        }

        Role ownerRole = roleRepository.findByName("OWNER")
                .orElseThrow();

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setNome("Admin");
        admin.setCognome("System");
        admin.setEmail("admin@test.com");
        admin.setCodiceFiscale("RSSMRA80A01H501U");
        admin.setStatus(UserStatus.ACTIVE);
        admin.setRoles(Set.of(ownerRole));

        userRepository.save(admin);
    }
}
