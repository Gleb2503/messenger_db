package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Auth.AuthRequest;
import org.example.dto.Auth.AuthResponse;
import org.example.entity.User;
import org.example.enums.UserStatus;
import org.example.repository.UserRepository;
import org.example.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "API для аутентификации и регистрации")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Пользователь уже существует")
    })
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "Данные для регистрации", required = true)
            @RequestBody AuthRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setStatus(UserStatus.offline);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(savedUser.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getUsername());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        // ❌ УДАЛЕНО: response.setApiKey(savedUser.getApiKey());
        response.setExpiresIn(86400L);
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Вход в систему")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Учетные данные", required = true)
            @RequestBody AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(request.getUsername());

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(86400L);
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Обновить токен доступа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токен обновлен"),
            @ApiResponse(responseCode = "401", description = "Неверный refresh токен")
    })
    public ResponseEntity<AuthResponse> refreshToken(
            @Parameter(description = "Refresh токен", required = true)
            @RequestBody String refreshToken) {

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        String newToken = jwtTokenProvider.generateToken(username);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        AuthResponse response = new AuthResponse();
        response.setToken(newToken);
        response.setRefreshToken(newRefreshToken);
        response.setExpiresIn(86400L);

        return ResponseEntity.ok(response);
    }
}