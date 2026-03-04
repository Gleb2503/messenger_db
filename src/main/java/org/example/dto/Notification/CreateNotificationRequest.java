package org.example.dto.Notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.Notification;
import org.example.entity.User;
import org.example.enums.NotificationType;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание уведомления")
public class CreateNotificationRequest {

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

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

    public Notification toEntity() {
        Notification notification = new Notification();
        if (this.userId != null) {
            User user = new User();
            user.setId(this.userId);
            notification.setUser(user);
        }
        if (this.type != null) {
            notification.setType(NotificationType.valueOf(this.type));
        }
        notification.setTitle(this.title);
        notification.setBody(this.body);
        notification.setEntityId(this.entityId);
        notification.setEntityType(this.entityType);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return notification;
    }
}