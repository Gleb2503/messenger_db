package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.Session;
import org.example.service.SessionService;
import org.example.exeption.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Sessions", description = "Управление сессиями")
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    @Operation(summary = "Получить все сессии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Session.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<Session>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить сессию по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сессия найдена"),
            @ApiResponse(responseCode = "404", description = "Сессия не найдена")
    })
    public ResponseEntity<Session> getSessionById(
            @Parameter(description = "Уникальный идентификатор сессии", required = true, example = "1")
            @PathVariable Long id) {
        return sessionService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить сессии пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<Session>> getSessionsByUserId(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(sessionService.getSessionsByUserId(userId));
    }

    @GetMapping("/user/{userId}/active")
    @Operation(summary = "Получить активные сессии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    [
                      {
                        "id": 1,
                        "user": {"id": 1},
                        "deviceName": "Chrome on Windows",
                        "deviceType": "desktop",
                        "isActive": true,
                        "lastActiveAt": "2026-02-27T17:50:00"
                      },
                      {
                        "id": 2,
                        "user": {"id": 1},
                        "deviceName": "Safari on iPhone",
                        "deviceType": "mobile",
                        "isActive": true,
                        "lastActiveAt": "2026-02-27T17:45:00"
                      }
                    ]
                    """))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<Session>> getActiveSessionsByUserId(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(sessionService.getActiveSessionsByUserId(userId));
    }

    @PostMapping
    @Operation(summary = "Создать сессию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Сессия успешно создана",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Session.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "user": {"id": 1, "username": "ivan_dev"},
                      "deviceName": "Chrome on Windows",
                      "deviceType": "desktop",
                      "ipAddress": "192.168.1.100",
                      "isActive": true,
                      "lastActiveAt": "2026-02-27T17:50:00",
                      "createdAt": "2026-02-27T17:50:00",
                      "expiresAt": "2026-03-23T14:00:00"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Session> createSession(
            @Parameter(description = "Данные для создания сессии", required = true)
            @RequestBody Session session) {
        return ResponseEntity.ok(sessionService.createSession(session));
    }

    @PutMapping("/{id}/logout")
    @Operation(summary = "Выйти из сессии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Выход выполнен успешно"),
            @ApiResponse(responseCode = "404", description = "Сессия не найдена")
    })
    public ResponseEntity<Void> logoutSession(
            @Parameter(description = "Уникальный идентификатор сессии", required = true, example = "1")
            @PathVariable Long id) {
        sessionService.logoutSession(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить сессию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Сессия успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Сессия не найдена")
    })
    public ResponseEntity<Void> deleteSession(
            @Parameter(description = "Уникальный идентификатор сессии", required = true, example = "1")
            @PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}