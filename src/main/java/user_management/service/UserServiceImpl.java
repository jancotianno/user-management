package user_management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import user_management.dto.CreateUserRequest;
import user_management.dto.UpdateUserRequest;
import user_management.dto.UserListResponse;
import user_management.dto.UserResponse;
import user_management.entity.Role;
import user_management.entity.User;
import user_management.enumeration.UserStatus;
import user_management.exception.ConflictException;
import user_management.exception.UserNotFoundException;
import user_management.mapper.UserMapper;
import user_management.repository.RoleRepository;
import user_management.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user - username={}, email={}",
                request.getUsername(),
                request.getEmail());

        // 1. check unicità email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already present");
        }

        // 2. check unicità codice fiscale
        if (userRepository.existsByCodiceFiscale(request.getCodiceFiscale())) {
            throw new ConflictException("Codice fiscale already present");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNome(request.getNome());
        user.setCognome(request.getCognome());
        user.setEmail(request.getEmail());
        user.setCodiceFiscale(request.getCodiceFiscale());

        // 3. gestione ruoli
        Set<Role> roles;

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {

            roles = request.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());

        } else {

            Role defaultRole = roleRepository.findByName("REPORTER")
                    .orElseThrow(() -> new RuntimeException("Default role REPORTER not configured"));

            roles = Set.of(defaultRole);
        }

        user.setRoles(roles);

        User saved = userRepository.save(user);

        log.info("User created successfully - id={}", saved.getId());

        return UserMapper.toResponse(saved);
    }

    public Page<UserResponse> getUsers(Pageable pageable) {

        Page<User> users = userRepository.findAllByStatusNot(
                UserStatus.DELETED,
                pageable
        );

        return users.map(UserMapper::toResponse);
    }

    public Page<UserListResponse> getAllUsers(Pageable pageable) {

        Page<User> users = userRepository.findAll(pageable);

        return users.map(user -> new UserListResponse(
                user.getId(),
                user.getUsername(),
                user.getNome(),
                user.getCognome()
        ));
    }

    public void deleteUser(Long id) {
        log.info("Soft deleting user - id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.DELETED);

        userRepository.save(user);
    }

    public UserResponse assignRole(Long userId, String roleName) {
        log.info("Assigning role={} to user - id={}", roleName, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);

        return UserMapper.toResponse(userRepository.save(user));
    }

    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return UserMapper.toResponse(user);
    }

    @CacheEvict(value = "users", key = "#id")
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user - id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setNome(request.getNome());
        user.setCognome(request.getCognome());
        user.setEmail(request.getEmail());

        // roles
        if (request.getRoles() != null) {
            Set<Role> roles = request.getRoles().stream()
                    .map(r -> roleRepository.findByName(r)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + r)))
                    .collect(Collectors.toSet());

            user.getRoles().clear();
            user.getRoles().addAll(roles);
        }

        return UserMapper.toResponse(userRepository.save(user));
    }
}
