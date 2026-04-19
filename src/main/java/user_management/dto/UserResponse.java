package user_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import user_management.annotation.MaskForRoles;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    @MaskForRoles({"REPORTER"})
    private String username;
    private String nome;
    private String cognome;
    @MaskForRoles({"REPORTER", "DEVELOPER"})
    private String email;
    private String codiceFiscale;
    private Set<String> roles;
}
