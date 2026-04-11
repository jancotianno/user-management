package user_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import user_management.dto.CreateUserRequest;
import user_management.dto.UpdateUserRequest;
import user_management.dto.UserResponse;
import user_management.entity.Role;
import user_management.entity.User;
import user_management.enumeration.UserStatus;
import user_management.exception.ConflictException;
import user_management.exception.UserNotFoundException;
import user_management.mapper.UserMapper;
import user_management.repository.RoleRepository;
import user_management.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserResponse createUser(CreateUserRequest request) {

        // 1. check unicità email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email già presente");
        }

        // 2. check unicità codice fiscale
        if (userRepository.existsByCodiceFiscale(request.getCodiceFiscale())) {
            throw new ConflictException("Codice fiscale già presente");
        }

        User user = new User();
        user.setNome(request.getNome());
        user.setCognome(request.getCognome());
        user.setEmail(request.getEmail());
        user.setCodiceFiscale(request.getCodiceFiscale());

        // 3. gestione ruoli
        Set<Role> roles;

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {

            roles = request.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role non trovato: " + roleName)))
                    .collect(Collectors.toSet());

        } else {

            Role defaultRole = roleRepository.findByName("REPORTER")
                    .orElseThrow(() -> new RuntimeException("Default role REPORTER non configurato"));

            roles = Set.of(defaultRole);
        }

        user.setRoles(roles);

        User saved = userRepository.save(user);

        return UserMapper.toResponse(saved);
    }

    public Page<UserResponse> getUsers(Pageable pageable) {

        Page<User> users = userRepository.findAllByStatusNot(
                UserStatus.DELETED,
                pageable
        );

        return users.map(UserMapper::toResponse);
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        user.setStatus(UserStatus.DELETED);

        userRepository.save(user);
    }

    public UserResponse assignRole(Long userId, String roleName) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User non trovato"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role non trovato"));

        user.getRoles().add(role);

        return UserMapper.toResponse(userRepository.save(user));
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User non trovato con id: " + id));

        return UserMapper.toResponse(user);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User non trovato"));

        // CHECK CF UNIQUE
        Optional<User> existing = userRepository.findByCodiceFiscaleAndIdNot(
                        request.getCodiceFiscale(), id
                );

        if (existing.isPresent()) {
            throw new ConflictException("Codice fiscale già associato ad un altro utente");
        }

        user.setNome(request.getNome());
        user.setCognome(request.getCognome());
        user.setEmail(request.getEmail());
        user.setCodiceFiscale(request.getCodiceFiscale());

        // roles
        if (request.getRoles() != null) {
            Set<Role> roles = request.getRoles().stream()
                    .map(r -> roleRepository.findByName(r)
                            .orElseThrow(() -> new RuntimeException("Role non trovato: " + r)))
                    .collect(Collectors.toSet());

            user.getRoles().clear();
            user.getRoles().addAll(roles);
        }

        return UserMapper.toResponse(userRepository.save(user));
    }
}
