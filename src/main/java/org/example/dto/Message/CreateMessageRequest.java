package org.example.dto.Message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.Message;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.enums.MessageType;
import org.example.enums.DeliveryStatus;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание сообщения")
public class CreateMessageRequest {

    @Schema(description = "ID чата", example = "1")
    private Long chatId;

    @Schema(description = "ID отправителя", example = "1")
    private Long senderId;

    @Schema(description = "Содержимое сообщения", example = "Привет всем!")
    private String content;

    @Schema(description = "Тип сообщения", example = "text")
    private String messageType;

    @Schema(description = "ID ответа на сообщение", example = "5")
    private Long replyToId;

    public Message toEntity() {
        Message message = new Message();
        if (this.chatId != null) {
            Chat chat = new Chat();
            chat.setId(this.chatId);
            message.setChat(chat);
        }
        if (this.senderId != null) {
            User sender = new User();
            sender.setId(this.senderId);
            message.setSender(sender);
        }
        if (this.replyToId != null) {
            Message replyTo = new Message();
            replyTo.setId(this.replyToId);
            message.setReplyTo(replyTo);
        }
        message.setContent(this.content);
        if (this.messageType != null) {
            message.setMessageType(MessageType.valueOf(this.messageType));
        } else {
            message.setMessageType(MessageType.text);
        }
        message.setIsEdited(false);
        message.setIsDeleted(false);
        message.setDeliveryStatus(DeliveryStatus.sent);
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        return message;
    }
}