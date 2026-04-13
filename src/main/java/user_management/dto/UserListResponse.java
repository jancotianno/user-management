package user_management.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserListResponse {
    private Long id;
    private String username;
    private String nome;
    private String cognome;
}
