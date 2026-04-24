package org.example.dto.Message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.dto.Attachment.AttachmentResponse;
import org.example.dto.Chat.ChatDTO;
import org.example.dto.User.UserDTO;
import org.example.enums.DeliveryStatus;
import org.example.enums.MessageType;

import java.time.LocalDateTime;
import java.util.List;

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
    @JsonProperty("text")
    private String content;

    @Schema(description = "Тип сообщения", example = "text")
    private MessageType messageType;

    @Schema(description = "Сообщение, на которое отвечаем")
    private ReplyToDTO replyTo;

    @Schema(description = "Отредактировано ли", example = "false")
    private Boolean isEdited;

    @Schema(description = "Удалено ли", example = "false")
    private Boolean isDeleted;

    @Schema(description = "Статус доставки", example = "sent")
    @JsonProperty("status")
    private DeliveryStatus deliveryStatus;

    @Schema(description = "Дата создания", example = "2026-03-08T10:00:00")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления")
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;


    @Schema(description = "Вложения (изображения, видео, файлы)")
    @JsonProperty("attachments")
    private List<AttachmentResponse> attachments;

    @JsonProperty("senderId")
    public Long getSenderId() {
        return sender != null ? sender.getId() : null;
    }
}