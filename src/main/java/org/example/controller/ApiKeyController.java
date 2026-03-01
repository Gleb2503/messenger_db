package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.User;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/api-keys")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "API Keys", description = "Управление персональными API ключами")
public class ApiKeyController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    @Operation(summary = "Получить мой API ключ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение ключа"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    public ResponseEntity<Map<String, Object>> getMyApiKey(
            @AuthenticationPrincipal User user) {

        Map<String, Object> response = new HashMap<>();
        response.put("apiKey", user.getApiKey());
        response.put("expiresAt", user.getApiKeyExpiresAt());
        response.put("createdAt", user.getCreatedAt());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/regenerate")
    @Operation(summary = "Сгенерировать новый API ключ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ключ успешно сгенерирован"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    public ResponseEntity<Map<String, Object>> regenerateApiKey(
            @AuthenticationPrincipal User user) {

        user.generateApiKey();
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("apiKey", user.getApiKey());
        response.put("expiresAt", user.getApiKeyExpiresAt());
        response.put("message", "API key regenerated successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/revoke")
    @Operation(summary = "Отозвать API ключ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ключ успешно отозван"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    public ResponseEntity<Map<String, Object>> revokeApiKey(
            @AuthenticationPrincipal User user) {

        user.setApiKey(null);
        user.setApiKeyExpiresAt(null);
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "API key revoked successfully");

        return ResponseEntity.ok(response);
    }
}