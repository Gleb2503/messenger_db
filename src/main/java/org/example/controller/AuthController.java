package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Auth.AuthResponse;
import org.example.dto.Auth.LoginRequest;
import org.example.dto.Auth.RegisterRequest;
import org.example.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS})
@Tag(name = "Auth", description = "Аутентификация и регистрация")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "409", description = "Пользователь уже существует")
    })
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "Данные регистрации", required = true)
            @Valid @RequestBody RegisterRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            log.warn("Register validation failed: {}", errors);
            return ResponseEntity.badRequest().build();
        }
        log.info("Register request: username={}, email={}, phone={}",
                request.getUsername(), request.getEmail(), request.getPhone());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Вход в систему по номеру телефона")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Неверные учётные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Данные входа (телефон + пароль)", required = true)
            @Valid @RequestBody LoginRequest request,
            BindingResult bindingResult) {
        log.info("=== Login request received ===");
        log.info("Request body: phone={}, passwordLength={}",
                request.getPhone(),
                request.getPassword() != null ? request.getPassword().length() : 0);
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            log.error("Login validation FAILED: {}", errors);
            return ResponseEntity.badRequest().build();
        }
        log.info("Login validation passed, calling service...");
        try {
            AuthResponse response = authService.login(request);
            log.info("Login successful for user: {}", response.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage(), e);
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                return ResponseEntity.status(404).build();
            }
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Обновление access-токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токен обновлён"),
            @ApiResponse(responseCode = "401", description = "Неверный refresh токен")
    })
    public ResponseEntity<AuthResponse> refresh(
            @Parameter(description = "Refresh токен", required = true)
            @RequestParam String refreshToken) {
        log.info("Refresh token request received");
        try {
            AuthResponse response = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Refresh failed: {}", e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("error", "Validation failed");
        errors.put("status", 400);
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        errors.put("details", fieldErrors);
        log.warn("Validation exception: {}", fieldErrors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseException(DataIntegrityViolationException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Database constraint violation");
        error.put("status", 409);

        String message = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        if (message != null) {
            if (message.contains("uk_users_phone")) {
                error.put("details", Map.of("phone", "Номер телефона уже зарегистрирован"));
            } else if (message.contains("uk_users_username")) {
                error.put("details", Map.of("username", "Имя пользователя уже занято"));
            } else if (message.contains("uk_users_email")) {
                error.put("details", Map.of("email", "Email уже зарегистрирован"));
            } else {
                error.put("details", Map.of("error", "Нарушение уникальности данных"));
            }
        }

        log.warn("Database constraint violation: {}", message);
        return ResponseEntity.status(409).body(error);
    }
}