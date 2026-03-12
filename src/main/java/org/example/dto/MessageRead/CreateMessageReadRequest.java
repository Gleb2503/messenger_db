package org.example.dto.MessageRead;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.entity.MessageRead;
import org.example.entity.Message;
import org.example.entity.User;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на отметку прочтения")
public class CreateMessageReadRequest {

    @NotNull(message = "messageId не может быть null")
    @Schema(description = "ID сообщения", example = "1", required = true)
    private Long messageId;

    @NotNull(message = "userId не может быть null")
    @Schema(description = "ID пользователя", example = "1", required = true)
    private Long userId;

    public MessageRead toEntity() {
        MessageRead messageRead = new MessageRead();

        if (this.messageId != null) {
            Message message = new Message();
            message.setId(this.messageId);
            messageRead.setMessage(message);
        }

        if (this.userId != null) {
            User user = new User();
            user.setId(this.userId);
            messageRead.setUser(user);
        }

        messageRead.setReadAt(LocalDateTime.now());

        return messageRead;
    }
}