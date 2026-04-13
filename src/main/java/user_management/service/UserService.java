package user_management.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import user_management.dto.CreateUserRequest;
import user_management.dto.UpdateUserRequest;
import user_management.dto.UserListResponse;
import user_management.dto.UserResponse;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    Page<UserResponse> getUsers(Pageable pageable);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);

    UserResponse assignRole(Long userId, String roleName);

    Page<UserListResponse> getAllUsers(Pageable pageable );
}
