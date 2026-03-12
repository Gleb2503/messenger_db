package org.example.dto.Notification;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;  // ✅ Импорт должен быть!
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.entity.Notification;
import org.example.entity.User;
import org.example.enums.NotificationType;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание уведомления")
public class CreateNotificationRequest {

    @NotNull(message = "userId не может быть null")
    @Schema(description = "ID пользователя", example = "1", required = true)
    private Long userId;

    @NotBlank(message = "type не может быть пустым")  // ✅ Обязательно!
    @Size(max = 30, message = "type не может превышать 30 символов")
    @Schema(description = "Тип уведомления", example = "new_message", required = true)
    private String type;

    @Size(max = 200, message = "title не может превышать 200 символов")
    @Schema(description = "Заголовок", example = "Новое сообщение")
    private String title;

    @Size(max = 500, message = "body не может превышать 500 символов")
    @Schema(description = "Текст уведомления", example = "Иван отправил вам сообщение")
    private String body;

    @Schema(description = "ID связанной сущности", example = "5")
    private Long entityId;

    @Size(max = 50, message = "entityType не может превышать 50 символов")
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