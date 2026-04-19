package user_management.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import user_management.auth.JwtUtil;
import user_management.dto.CreateUserRequest;
import user_management.entity.Role;
import user_management.entity.User;
import user_management.repository.RoleRepository;
import user_management.repository.UserRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerPostIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private String ownerToken;

    @BeforeEach
    void setup() {
        // 1. Creo ruolo OWNER se non esiste
        Role ownerRole = roleRepository.findByName("OWNER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("OWNER");
                    return roleRepository.save(r);
                });

        // 2. Creo utente OWNER
        User owner = new User();
        owner.setUsername("owner");
        owner.setPassword("password");
        owner.setEmail("owner@test.com");
        owner.setNome("Owner");
        owner.setCognome("User");
        owner.setCodiceFiscale("OWNUSR90A01H501X");
        owner.setRoles(Set.of(ownerRole));

        userRepository.save(owner);

        // 3. Genero JWT valido usando la tua JwtUtil
        ownerToken = jwtUtil.generateToken(owner);
    }

    @Test
    void createUser_shouldReturn201AndPersistUser() throws Exception {

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("newuser");
        request.setPassword("Password123!");
        request.setNome("Mario");
        request.setCognome("Rossi");
        request.setEmail("mario.rossi@example.com");
        request.setCodiceFiscale("RSSMRA90A01H501X");
        request.setRoles(Set.of("REPORTER"));

        mockMvc.perform(
                        post("/api/users")
                                .header("Authorization", "Bearer " + ownerToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("mario.rossi@example.com"));

        // Verifica che l’utente sia stato salvato
        assertTrue(userRepository.existsByEmail("mario.rossi@example.com"));
    }

    private static String asJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}

