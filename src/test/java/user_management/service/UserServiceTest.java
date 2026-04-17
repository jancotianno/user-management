package user_management.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import user_management.dto.CreateUserRequest;
import user_management.dto.UserResponse;
import user_management.entity.Role;
import user_management.entity.User;
import user_management.event.producer.UserEventProducer;
import user_management.exception.UserNotFoundException;
import user_management.repository.RoleRepository;
import user_management.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUserSuccessfully() {

        // GIVEN
        CreateUserRequest request = new CreateUserRequest(
                "mario23",
                "1234",
                "Mario",
                "Rossi",
                "mario@test.com",
                "RSSMRA80A01H501U",
                Set.of("REPORTER")
        );

        Role role = new Role();
        role.setName("REPORTER");

        User saved = new User();
        saved.setId(1L);
        saved.setUsername("mario23");
        saved.setEmail("mario@test.com");
        saved.setNome("Mario");
        saved.setCognome("Rossi");
        saved.setCodiceFiscale("RSSMRA80A01H501U");

        // MOCK fondamentali
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByCodiceFiscale(anyString())).thenReturn(false);

        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

        when(roleRepository.findByName(anyString()))
                .thenReturn(Optional.of(role));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    u.setId(1L);
                    return u;
                });

        // WHEN
        UserResponse result = userService.createUser(request);

        // THEN
        assertNotNull(result);
        assertEquals("mario23", result.getUsername());
        assertEquals(1L, result.getId());

        verify(userRepository).save(any(User.class));
        verify(userEventProducer).sendUserCreatedEvent(any());
    }

    @Test
    void shouldThrowWhenUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }
}