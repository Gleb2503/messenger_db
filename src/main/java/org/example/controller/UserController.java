package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.User.CreateUserRequest;
import org.example.dto.User.UpdateUserRequest;
import org.example.dto.User.UserResponse;
import org.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Users", description = "Управление пользователями")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получить последние 100 пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<UserResponse>> getLast100Users() {
        List<UserResponse> response = userService.getLast100Users();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Получить пользователя по имени")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserResponse> getUserByUsername(
            @Parameter(description = "Имя пользователя", required = true, example = "ivan_dev")
            @PathVariable String username) {
        UserResponse response = userService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск пользователей по имени")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<UserResponse>> searchUsers(
            @Parameter(description = "Часть имени", required = true, example = "ivan")
            @RequestParam String username) {
        List<UserResponse> response = userService.searchUsersByUsername(username);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Создать пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "Данные пользователя", required = true)
            @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Обновлённые данные", required = true)
            @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}