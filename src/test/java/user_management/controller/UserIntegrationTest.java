package user_management.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import user_management.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(roles = "OWNER")
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    //@MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "OPERATOR")
    void shouldReturnForbiddenIfWrongRole() throws Exception {

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/sql/data.sql")
    void shouldReturnUserById() throws Exception {

        Long userId = 1L;

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("mario23"));
    }
}
