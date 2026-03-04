package org.example.dto.Message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.dto.Chat.ChatDTO;
import org.example.dto.User.UserDTO;
import org.example.enums.MessageType;
import org.example.enums.DeliveryStatus;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с информацией о сообщении")
public class MessageResponse {

    @Schema(description = "ID сообщения", example = "1")
    private Long id;

    @Schema(description = "Чат")
    private ChatDTO chat;

    @Schema(description = "Отправитель")
    private UserDTO sender;

    @Schema(description = "Содержимое", example = "Привет всем!")
    private String content;

    @Schema(description = "Тип сообщения", example = "text")
    private MessageType messageType;

    @Schema(description = "Отредактировано ли", example = "false")
    private Boolean isEdited;

    @Schema(description = "Удалено ли", example = "false")
    private Boolean isDeleted;

    @Schema(description = "Статус доставки", example = "sent")
    private DeliveryStatus deliveryStatus;

    @Schema(description = "Дата создания", example = "2026-03-03T14:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления")
    private LocalDateTime updatedAt;
}