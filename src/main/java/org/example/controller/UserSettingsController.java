package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.UserSettings.CreateUserSettingsRequest;
import org.example.dto.UserSettings.UserSettingsResponse;
import org.example.entity.UserSettings;
import org.example.service.UserSettingsService;
import org.example.exeption.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "UserSettings", description = "Настройки пользователя")
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    @GetMapping
    @Operation(summary = "Получить все настройки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<UserSettingsResponse>> getAllSettings() {
        List<UserSettings> settings = userSettingsService.getAllSettings();
        List<UserSettingsResponse> response = settings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить настройки по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Настройки найдены"),
            @ApiResponse(responseCode = "404", description = "Настройки не найдены")
    })
    public ResponseEntity<UserSettingsResponse> getSettingsById(
            @Parameter(description = "ID настроек", required = true, example = "1")
            @PathVariable Long id) {
        UserSettings settings = userSettingsService.getSettingsById(id);
        return ResponseEntity.ok(convertToResponse(settings));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить настройки пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение"),
            @ApiResponse(responseCode = "404", description = "Настройки не найдены")
    })
    public ResponseEntity<UserSettingsResponse> getSettingsByUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        UserSettings settings = userSettingsService.getSettingsByUser(userId);
        return ResponseEntity.ok(convertToResponse(settings));
    }

    @PostMapping
    @Operation(summary = "Создать настройки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Настройки созданы"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<UserSettingsResponse> createSettings(
            @Parameter(description = "Данные настроек", required = true)
            @RequestBody CreateUserSettingsRequest request) {
        UserSettings settings = userSettingsService.createSettings(request.toEntity());
        return ResponseEntity.status(201).body(convertToResponse(settings));
    }

    @PutMapping("/user/{userId}")
    @Operation(summary = "Обновить настройки пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Настройки обновлены"),
            @ApiResponse(responseCode = "404", description = "Настройки не найдены")
    })
    public ResponseEntity<UserSettingsResponse> updateSettings(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Обновлённые данные", required = true)
            @RequestBody CreateUserSettingsRequest request) {
        UserSettings settings = userSettingsService.updateSettings(userId, request.toEntity());
        return ResponseEntity.ok(convertToResponse(settings));
    }

    @DeleteMapping("/user/{userId}")
    @Operation(summary = "Удалить настройки пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Настройки удалены"),
            @ApiResponse(responseCode = "404", description = "Настройки не найдены")
    })
    public ResponseEntity<Void> deleteSettings(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        userSettingsService.deleteSettings(userId);
        return ResponseEntity.noContent().build();
    }

    private UserSettingsResponse convertToResponse(UserSettings settings) {
        UserSettingsResponse response = new UserSettingsResponse();
        response.setId(settings.getId());
        response.setPushEnabled(settings.getPushEnabled());
        response.setEmailEnabled(settings.getEmailEnabled());
        response.setSoundEnabled(settings.getSoundEnabled());
        response.setTheme(settings.getTheme());
        response.setLanguage(settings.getLanguage());
        response.setCreatedAt(settings.getCreatedAt());
        response.setUpdatedAt(settings.getUpdatedAt());

        if (settings.getUser() != null) {
            response.setUser(convertUserToDTO(settings.getUser()));
        }

        return response;
    }

    private org.example.dto.User.UserDTO convertUserToDTO(org.example.entity.User user) {
        org.example.dto.User.UserDTO dto = new org.example.dto.User.UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}