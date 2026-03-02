package org.example.dto.Notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание уведомления")
public class CreateNotificationRequest {

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "Тип уведомления", example = "new_message", allowableValues = {"new_message", "friend_request", "system", "call", "mention", "group_invite"})
    private String type;

    @Schema(description = "Заголовок", example = "Новое сообщение")
    private String title;

    @Schema(description = "Текст уведомления", example = "Иван отправил вам сообщение")
    private String body;

    @Schema(description = "ID связанной сущности", example = "5")
    private Long entityId;

    @Schema(description = "Тип сущности", example = "message")
    private String entityType;
}