package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Notification.CreateNotificationRequest;
import org.example.dto.Notification.NotificationResponse;
import org.example.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Notifications", description = "Управление уведомлениями")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Получить последние 100 уведомлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<NotificationResponse>> getLast100Notifications() {
        List<NotificationResponse> response = notificationService.getLast100Notifications();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить уведомление по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомление найдено"),
            @ApiResponse(responseCode = "404", description = "Уведомление не найдено")
    })
    public ResponseEntity<NotificationResponse> getNotificationById(
            @Parameter(description = "ID уведомления", required = true, example = "1")
            @PathVariable Long id) {
        NotificationResponse response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить последние 100 уведомлений пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<NotificationResponse>> getLast100NotificationsByUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        List<NotificationResponse> response = notificationService.getLast100NotificationsByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Получить последние 100 непрочитанных уведомлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<NotificationResponse>> getLast100UnreadByUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        List<NotificationResponse> response = notificationService.getLast100UnreadByUser(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Создать уведомление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Уведомление создано"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<NotificationResponse> createNotification(
            @Parameter(description = "Данные уведомления", required = true)
            @Valid @RequestBody CreateNotificationRequest request) {  // ✅ ДОБАВЛЕНО @Valid!

        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Отметить уведомление прочитанным")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомление прочитано"),
            @ApiResponse(responseCode = "404", description = "Уведомление не найдено")
    })
    public ResponseEntity<NotificationResponse> markAsRead(
            @Parameter(description = "ID уведомления", required = true, example = "1")
            @PathVariable Long id) {
        NotificationResponse response = notificationService.markAsRead(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить уведомление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Уведомление удалено"),
            @ApiResponse(responseCode = "404", description = "Уведомление не найдено")
    })
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "ID уведомления", required = true, example = "1")
            @PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}