package org.example.dto.MessageRead;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о прочтении сообщения")
public class MessageReadResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "ID сообщения")
    private Long messageId;

    @Schema(description = "ID пользователя")
    private Long userId;

    @Schema(description = "Дата прочтения", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime readAt;
}