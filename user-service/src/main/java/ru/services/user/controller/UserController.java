package ru.services.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.services.user.mapper.UserMapper;
import ru.services.user.model.UserRequest;
import ru.services.user.model.UserResponse;
import ru.services.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @Operation(summary = "Get user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User info"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(value = "/{id}")
    public UserResponse get(@PathVariable("id") Long id) {
        return mapper.toResponse(userService.getUser(id));
    }

    @Operation(summary = "Get user by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User info"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    public UserResponse get(@RequestParam String username) {
        return mapper.toResponse(userService.getUser(username));
    }

    @Operation(summary = "Delete user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @Operation(summary = "Delete user by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("username") String username) {
        userService.deleteUser(username);
    }

    @Operation(summary = "Create user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody @Valid UserRequest request) {
        var user = userService.createUser(mapper.toUser(request));
        return mapper.toResponse(user);
    }

    @Operation(summary = "Update user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping
    public UserResponse update(@RequestBody @Valid UserRequest request) {
        var user = userService.updateUser(mapper.toUser(request));
        return mapper.toResponse(user);
    }
}