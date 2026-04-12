package user_management.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import user_management.dto.CreateRoleRequest;
import user_management.entity.Role;
import user_management.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public Role createRole(@Valid @RequestBody CreateRoleRequest request) {
        return roleService.createRole(request.getName());
    }

    @GetMapping
    public List<Role> getRoles() {
        return roleService.getAllRoles();
    }
}
