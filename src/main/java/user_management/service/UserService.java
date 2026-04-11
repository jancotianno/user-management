package user_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user_management.dto.CreateUserRequest;
import user_management.dto.UserResponse;
import user_management.entity.User;
import user_management.enumeration.UserStatus;
import user_management.exception.ConflictException;
import user_management.mapper.UserMapper;
import user_management.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email già presente");
        }

        if (userRepository.existsByCodiceFiscale(request.getCodiceFiscale())) {
            throw new ConflictException("Codice fiscale già presente");
        }

        User user = User.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                .codiceFiscale(request.getCodiceFiscale())
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        return UserMapper.toResponse(userRepository.save(user));
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
}
