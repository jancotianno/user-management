package user_management.mapper;

import user_management.dto.UserResponse;
import user_management.entity.Role;
import user_management.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nome(user.getNome())
                .cognome(user.getCognome())
                .email(user.getEmail())
                .codiceFiscale(user.getCodiceFiscale())
                .roles(
                        user.getRoles() == null
                                ? Set.of()
                                : user.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
