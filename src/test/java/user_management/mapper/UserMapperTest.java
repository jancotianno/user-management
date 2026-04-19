package user_management.mapper;

import org.junit.jupiter.api.Test;
import user_management.dto.UserResponse;
import user_management.entity.Role;
import user_management.entity.User;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toResponse_shouldMapAllFieldsCorrectly() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("teo");
        user.setNome("Teodoro");
        user.setCognome("Rossi");
        user.setEmail("teo@example.com");
        user.setCodiceFiscale("RSSTDR90A01H501X");

        Role role1 = new Role();
        role1.setName("OWNER");

        Role role2 = new Role();
        role2.setName("DEVELOPER");

        user.setRoles(Set.of(role1, role2));

        // Act
        UserResponse response = UserMapper.toResponse(user);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("teo", response.getUsername());
        assertEquals("Teodoro", response.getNome());
        assertEquals("Rossi", response.getCognome());
        assertEquals("teo@example.com", response.getEmail());
        assertEquals("RSSTDR90A01H501X", response.getCodiceFiscale());

        assertEquals(2, response.getRoles().size());
        assertTrue(response.getRoles().contains("OWNER"));
        assertTrue(response.getRoles().contains("DEVELOPER"));
    }

    @Test
    void toResponse_shouldHandleNullRoles() {
        // Arrange
        User user = new User();
        user.setId(2L);
        user.setUsername("mario");
        user.setRoles(null);

        // Act
        UserResponse response = UserMapper.toResponse(user);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getRoles());
        assertTrue(response.getRoles().isEmpty());
    }
}

