package org.example.dto.Reaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на добавление реакции")
public class CreateReactionRequest {

    @Schema(description = "ID сообщения", example = "1")
    private Long messageId;

    @Schema(description = "ID пользователя", example = "2")
    private Long userId;

    @Schema(description = "Эмодзи реакции", example = "👍")
    private String emoji;
}