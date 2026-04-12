package user_management.service;

import user_management.entity.Role;

import java.util.List;

public interface RoleService {

    Role createRole(String name);

    List<Role> getAllRoles();
}
