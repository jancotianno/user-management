package user_management.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
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
