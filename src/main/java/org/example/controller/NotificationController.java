package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.Notification;
import org.example.service.NotificationService;
import org.example.exeption.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Notifications", description = "Управление уведомлениями")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Получить все уведомления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Notification.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить уведомление по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомление найдено"),
            @ApiResponse(responseCode = "404", description = "Уведомление не найдено")
    })
    public ResponseEntity<Notification> getNotificationById(
            @Parameter(description = "Уникальный идентификатор уведомления", required = true, example = "1")
            @PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить уведомления пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<Notification>> getNotificationsByUserId(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Получить непрочитанные уведомления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    [
                      {
                        "id": 1,
                        "type": "new_message",
                        "title": "Новое сообщение",
                        "body": "Иван отправил вам сообщение",
                        "entityId": 5,
                        "entityType": "message",
                        "isRead": false,
                        "createdAt": "2026-02-27T17:30:00"
                      }
                    ]
                    """))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<Notification>> getUnreadNotifications(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @PostMapping
    @Operation(summary = "Создать уведомление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Уведомление успешно создано"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Notification> createNotification(
            @Parameter(description = "Данные для создания уведомления", required = true)
            @RequestBody Notification notification) {
        return ResponseEntity.ok(notificationService.createNotification(notification));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Отметить уведомление как прочитанное")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уведомление отмечено как прочитанное",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "type": "new_message",
                      "title": "Новое сообщение",
                      "isRead": true,
                      "readAt": "2026-02-27T17:40:00"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Уведомление не найдено")
    })
    public ResponseEntity<Notification> markAsRead(
            @Parameter(description = "Уникальный идентификатор уведомления", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить уведомление")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Уведомление успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Уведомление не найдено")
    })
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "Уникальный идентификатор уведомления", required = true, example = "1")
            @PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}