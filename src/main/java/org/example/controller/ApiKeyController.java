package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.service.ApiKeyService;
import org.example.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/api-keys")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "API Keys", description = "Управление персональными API ключами")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/me")
    @Operation(summary = "Получить мой API ключ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение ключа"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    public ResponseEntity<Map<String, Object>> getMyApiKey(HttpServletRequest httpRequest) {

        Long userId = getCurrentUserId(httpRequest);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        Map<String, Object> response = apiKeyService.getApiKeyInfo(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/regenerate")
    @Operation(summary = "Сгенерировать новый API ключ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ключ успешно сгенерирован"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    public ResponseEntity<Map<String, Object>> regenerateApiKey(HttpServletRequest httpRequest) {

        Long userId = getCurrentUserId(httpRequest);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        Map<String, Object> response = apiKeyService.regenerateApiKey(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/revoke")
    @Operation(summary = "Отозвать API ключ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ключ успешно отозван"),
            @ApiResponse(responseCode = "401", description = "Неавторизован")
    })
    public ResponseEntity<Map<String, Object>> revokeApiKey(HttpServletRequest httpRequest) {

        Long userId = getCurrentUserId(httpRequest);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        apiKeyService.revokeApiKey(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "API key revoked successfully");
        return ResponseEntity.ok(response);
    }

    private Long getCurrentUserId(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}