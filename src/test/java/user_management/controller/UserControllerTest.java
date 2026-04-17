package user_management.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import user_management.dto.CreateUserRequest;
import user_management.dto.UserResponse;
import user_management.service.UserService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldCreateUserSuccessfully() {

        // GIVEN
        CreateUserRequest request = new CreateUserRequest("mario23", "1234", "mario",
                "rossi", "mario@test.com", "RSSMRA80A01H501U", Set.of("ruolo"));

        UserResponse response = new UserResponse(1L, "mario23", "mario",
                "rossi", "mario@test.com", "RSSMRA80A01H501U", Set.of("ruolo"));

        when(userService.createUser(any(CreateUserRequest.class)))
                .thenReturn(response);
        // WHEN
        ResponseEntity<UserResponse> result = userController.createUser(request);

        // THEN
        assertEquals("mario23", result.getBody().getUsername());
        assertEquals(1L, result.getBody().getId());

        verify(userService).createUser(any(CreateUserRequest.class));
    }
}
