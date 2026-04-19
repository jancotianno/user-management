package user_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import user_management.annotation.MaskForRoles;

@AllArgsConstructor
@Data
public class UserListResponse {
    private Long id;
    @MaskForRoles({"REPORTER"})
    private String username;
    private String nome;
    private String cognome;
    @MaskForRoles({"REPORTER", "DEVELOPER"})
    private String email;
}
