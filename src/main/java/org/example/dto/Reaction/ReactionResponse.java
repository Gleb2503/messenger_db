package org.example.dto.Reaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.dto.Message.MessageDTO;
import org.example.dto.User.UserDTO;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с информацией о реакции")
public class ReactionResponse {

    @Schema(description = "ID реакции", example = "1")
    private Long id;

    @Schema(description = "Сообщение")
    private MessageDTO message;

    @Schema(description = "Пользователь")
    private UserDTO user;

    @Schema(description = "Эмодзи", example = "👍")
    private String emoji;

    @Schema(description = "Дата создания", example = "2026-03-03T14:00:00")
    private LocalDateTime createdAt;
}