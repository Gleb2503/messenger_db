package org.example.dto.Notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация об уведомлении")
public class NotificationResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "Тип уведомления", example = "new_message")
    private String type;

    @Schema(description = "Заголовок", example = "Новое сообщение")
    private String title;

    @Schema(description = "Текст уведомления", example = "Иван отправил вам сообщение")
    private String body;

    @Schema(description = "ID связанной сущности", example = "5")
    private Long entityId;

    @Schema(description = "Тип сущности", example = "message")
    private String entityType;

    @Schema(description = "Прочитано ли", example = "false")
    private Boolean isRead;

    @Schema(description = "Дата создания", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime createdAt;

    @Schema(description = "Дата прочтения", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime readAt;
}