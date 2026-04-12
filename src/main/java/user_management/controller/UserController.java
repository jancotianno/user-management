package user_management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import user_management.dto.AssignRoleRequest;
import user_management.dto.CreateUserRequest;
import user_management.dto.UpdateUserRequest;
import user_management.dto.UserResponse;
import user_management.service.UserService;

@Tag(name = "Users", description = "API gestione utenti")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('OWNER','OPERATOR')")
    @Operation(
            summary = "Creazione nuovo utente",
            description = "Crea un nuovo utente nel sistema con assegnazione opzionale dei ruoli. Se nessun ruolo viene specificato, viene assegnato il ruolo di default."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @PreAuthorize("hasAnyRole('OWNER','DEVELOPER','REPORTER')")
    @Operation(
            summary = "Lista utenti",
            description = "Restituisce una lista paginata di utenti attivi (esclusi quelli soft-deleted), con possibilità di ordinamento e filtraggio futuro."
    )
    @GetMapping
    public Page<UserResponse> getUsers(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @PreAuthorize("hasAnyRole('OWNER','DEVELOPER','REPORTER')")
    @Operation(
            summary = "Dettaglio utente",
            description = "Recupera i dettagli completi di un utente specifico tramite ID, inclusi i ruoli associati."
    )
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAnyRole('OWNER','OPERATOR')")
    @Operation(
            summary = "Disattivazione utente",
            description = "Esegue una cancellazione logica dell'utente impostando lo stato a DELETED. L'utente non viene rimosso fisicamente dal database."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PreAuthorize("hasRole('OWNER')")
    @Operation(
            summary = "Assegnazione ruoli utente",
            description = "Esegue un aggiornamento dei ruoli per l'utente."
    )
    @PostMapping("/{id}/roles")
    public UserResponse assignRole(@PathVariable Long id, @RequestBody AssignRoleRequest request) {
        return userService.assignRole(id, request.getRoleName());
    }

    @PreAuthorize("hasAnyRole('OWNER','OPERATOR','MAINTAINER')")
    @Operation(
            summary = "Aggiornamento utente",
            description = "Aggiorna i dati anagrafici e/o i ruoli di un utente. La modifica dei ruoli è sostitutiva (replace completo)."
    )
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }
}
