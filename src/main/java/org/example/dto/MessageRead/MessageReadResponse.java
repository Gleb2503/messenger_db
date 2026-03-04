package org.example.dto.MessageRead;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.dto.Message.MessageDTO;
import org.example.dto.User.UserDTO;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с информацией о прочтении")
public class MessageReadResponse {

    @Schema(description = "ID записи", example = "1")
    private Long id;

    @Schema(description = "Сообщение")
    private MessageDTO message;

    @Schema(description = "Пользователь")
    private UserDTO user;

    @Schema(description = "Дата прочтения", example = "2026-03-03T14:00:00")
    private LocalDateTime readAt;
}