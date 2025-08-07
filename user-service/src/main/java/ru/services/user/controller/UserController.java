package ru.services.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.services.user.authorization.AccessControlService;
import ru.services.user.mapper.UserMapper;
import ru.services.user.model.UserRequest;
import ru.services.user.model.UserResponse;
import ru.services.user.service.UserService;

@RestController
@RequestMapping("/api/users/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AccessControlService accessControlService;
    private final UserMapper mapper;

    @Operation(summary = "Get user by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User info"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public UserResponse get(
            @RequestParam String username,
            Authentication authentication) {

        String clientId = authentication.getName();

        if (!accessControlService.checkUsernameMatches(clientId, username)) {
            throw new AccessDeniedException("Access denied");
        }
        return mapper.toResponse(userService.getUser(username));
    }

    @Operation(summary = "Delete user by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam("username") String username,
            Authentication authentication) {

        String clientId = authentication.getName();

        if (!accessControlService.checkUsernameMatches(clientId, username)) {
            throw new AccessDeniedException("Access denied");
        }

        userService.deleteUser(username);
    }

    @Operation(summary = "Create user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(
            @RequestBody @Valid UserRequest request,
            @RequestHeader("X-Request-ID") String idempotencyKey,
            Authentication authentication) {

        String clientId = authentication.getName();

        if (!accessControlService.checkUsernameMatches(clientId, request.username())) {
            throw new AccessDeniedException("Access denied");
        }

        var user = userService.createUser(mapper.toUser(request), idempotencyKey);
        return mapper.toResponse(user);
    }

    @Operation(summary = "Update user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping
    public UserResponse update(
            @RequestBody @Valid UserRequest request,
            Authentication authentication) {

        String clientId = authentication.getName();

        if (!accessControlService.checkUsernameMatches(clientId, request.username())) {
            throw new AccessDeniedException("Access denied");
        }
        var user = userService.updateUser(mapper.toUser(request));
        return mapper.toResponse(user);
    }
}