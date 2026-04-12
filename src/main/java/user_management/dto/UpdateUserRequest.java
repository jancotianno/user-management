package user_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import user_management.validation.ValidCodiceFiscale;

import java.util.Set;

@Data
public class UpdateUserRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @Email
    @NotBlank
    private String email;

    private Set<String> roles;
}
