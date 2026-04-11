package user_management.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user_management.entity.Role;
import user_management.repository.RoleRepository;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles() {
        return args -> {

            // inizializza SOLO se DB vuoto
            if (roleRepository.count() == 0) {

                List<String> defaultRoles = List.of(
                        "OWNER",
                        "OPERATOR",
                        "MAINTAINER",
                        "DEVELOPER",
                        "REPORTER"
                );

                defaultRoles.forEach(roleName ->
                        roleRepository.save(
                                Role.builder()
                                        .name(roleName)
                                        .build()
                        )
                );
            }
        };
    }
}
