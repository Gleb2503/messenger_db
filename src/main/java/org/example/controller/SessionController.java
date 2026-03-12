package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Session.CreateSessionRequest;
import org.example.dto.Session.SessionResponse;
import org.example.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Sessions", description = "Управление сессиями")
public class SessionController {

    private final SessionService sessionService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить последние 100 сессий пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<SessionResponse>> getLast100SessionsByUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        List<SessionResponse> response = sessionService.getLast100SessionsByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить сессию по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сессия найдена"),
            @ApiResponse(responseCode = "404", description = "Сессия не найдена")
    })
    public ResponseEntity<SessionResponse> getSessionById(
            @Parameter(description = "ID сессии", required = true, example = "1")
            @PathVariable Long id) {
        SessionResponse response = sessionService.getSessionById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Создать новую сессию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Сессия создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<SessionResponse> createSession(
            @Parameter(description = "Данные сессии", required = true)
            @Valid @RequestBody CreateSessionRequest request) {

        SessionResponse response = sessionService.createSession(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}/active")
    @Operation(summary = "Обновить время активности")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сессия обновлена"),
            @ApiResponse(responseCode = "404", description = "Сессия не найдена")
    })
    public ResponseEntity<SessionResponse> updateLastActive(
            @Parameter(description = "ID сессии", required = true, example = "1")
            @PathVariable Long id) {
        SessionResponse response = sessionService.updateLastActive(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Отозвать сессию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Сессия отозвана"),
            @ApiResponse(responseCode = "404", description = "Сессия не найдена")
    })
    public ResponseEntity<Void> revokeSession(
            @Parameter(description = "ID сессии", required = true, example = "1")
            @PathVariable Long id) {
        sessionService.revokeSession(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}/all")
    @Operation(summary = "Отозвать все сессии пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Все сессии отозваны")
    })
    public ResponseEntity<Void> revokeAllUserSessions(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        sessionService.revokeAllUserSessions(userId);
        return ResponseEntity.noContent().build();
    }
}