package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.UserSettings;
import org.example.service.UserSettingsService;
import org.example.exeption.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "User Settings", description = "Настройки пользователя")
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    @GetMapping
    @Operation(summary = "Получить все настройки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSettings.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Iterable<UserSettings>> getAllSettings() {
        return ResponseEntity.ok(userSettingsService.getAllSettings());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить настройки по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Настройки найдены"),
            @ApiResponse(responseCode = "404", description = "Настройки не найдены")
    })
    public ResponseEntity<UserSettings> getSettingsById(
            @Parameter(description = "Уникальный идентификатор настроек", required = true, example = "1")
            @PathVariable Long id) {
        return userSettingsService.getSettingsById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Settings not found with id: " + id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить настройки пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Настройки найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSettings.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "user": {"id": 1, "username": "ivan_dev"},
                      "pushEnabled": true,
                      "emailEnabled": false,
                      "soundEnabled": true,
                      "theme": "dark",
                      "language": "ru",
                      "createdAt": "2026-02-27T17:00:00",
                      "updatedAt": "2026-02-27T17:45:00"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Настройки не найдены")
    })
    public ResponseEntity<UserSettings> getSettingsByUserId(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return userSettingsService.getSettingsByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Settings not found for user: " + userId));
    }

    @PostMapping
    @Operation(summary = "Создать настройки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Настройки успешно созданы"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<UserSettings> createSettings(
            @Parameter(description = "Данные для создания настроек", required = true)
            @RequestBody UserSettings settings) {
        return ResponseEntity.ok(userSettingsService.createSettings(settings));
    }

    @PutMapping("/user/{userId}")
    @Operation(summary = "Обновить настройки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Настройки успешно обновлены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSettings.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "user": {"id": 1},
                      "pushEnabled": true,
                      "emailEnabled": false,
                      "soundEnabled": true,
                      "theme": "dark",
                      "language": "ru",
                      "updatedAt": "2026-02-27T17:45:00"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Настройки не найдены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<UserSettings> updateSettings(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Обновленные данные настроек", required = true)
            @RequestBody UserSettings settings) {
        return ResponseEntity.ok(userSettingsService.updateSettings(userId, settings));
    }

    @DeleteMapping("/user/{userId}")
    @Operation(summary = "Удалить настройки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Настройки успешно удалены"),
            @ApiResponse(responseCode = "404", description = "Настройки не найдены")
    })
    public ResponseEntity<Void> deleteSettings(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        userSettingsService.deleteSettings(userId);
        return ResponseEntity.noContent().build();
    }
}