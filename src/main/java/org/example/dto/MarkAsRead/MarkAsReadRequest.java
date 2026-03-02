package org.example.dto.MarkAsRead;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на отметку сообщения как прочитанного")
public class MarkAsReadRequest {

    @Schema(description = "ID сообщения", example = "1")
    private Long messageId;

    @Schema(description = "ID пользователя", example = "2")
    private Long userId;
}