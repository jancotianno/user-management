package user_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import user_management.validation.ValidCodiceFiscale;

import java.util.Set;

@Data
public class CreateUserRequest {

    @Size(min = 3, max = 50)
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    @Email
    private String email;

    @ValidCodiceFiscale
    @NotBlank
    private String codiceFiscale;

    private Set<String> roles;
}
