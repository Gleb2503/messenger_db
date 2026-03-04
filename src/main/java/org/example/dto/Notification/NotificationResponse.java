package org.example.dto.Notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.dto.User.UserDTO;
import org.example.enums.NotificationType;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с информацией об уведомлении")
public class NotificationResponse {

    @Schema(description = "ID уведомления", example = "1")
    private Long id;

    @Schema(description = "Пользователь")
    private UserDTO user;

    @Schema(description = "Тип уведомления", example = "new_message")
    private NotificationType type;

    @Schema(description = "Заголовок", example = "Новое сообщение")
    private String title;

    @Schema(description = "Текст", example = "Иван отправил вам сообщение")
    private String body;

    @Schema(description = "ID сущности", example = "5")
    private Long entityId;

    @Schema(description = "Тип сущности", example = "message")
    private String entityType;

    @Schema(description = "Прочитано ли", example = "false")
    private Boolean isRead;

    @Schema(description = "Дата создания", example = "2026-03-03T14:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Дата прочтения")
    private LocalDateTime readAt;
}