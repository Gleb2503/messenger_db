package org.example.dto.Message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о сообщении")
public class MessageResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "Информация о чате")
    private ChatInfo chat;

    @Schema(description = "Информация об отправителе")
    private UserInfo sender;

    @Schema(description = "Текст сообщения", example = "Привет всем!")
    private String content;

    @Schema(description = "Тип сообщения", example = "text")
    private String messageType;

    @Schema(description = "Было ли отредактировано", example = "false")
    private Boolean isEdited;

    @Schema(description = "Было ли удалено", example = "false")
    private Boolean isDeleted;

    @Schema(description = "Статус доставки", example = "sent", allowableValues = {"sent", "delivered", "read"})
    private String deliveryStatus;

    @Schema(description = "Дата создания", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime updatedAt;

    @Data
    @Schema(description = "Краткая информация о чате")
    public static class ChatInfo {
        private Long id;
        private String name;
    }

    @Data
    @Schema(description = "Краткая информация о пользователе")
    public static class UserInfo {
        private Long id;
        private String username;
        private String displayName;
    }
}