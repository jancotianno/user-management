package user_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String nome;
    private String cognome;
    private String email;
    private String codiceFiscale;
    private Set<String> roles;
}
