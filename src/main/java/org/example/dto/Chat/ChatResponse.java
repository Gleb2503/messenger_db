package org.example.dto.Chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.dto.User.UserDTO;
import org.example.enums.ChatType;

import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с информацией о чате")
public class ChatResponse {

    @Schema(description = "ID чата", example = "1")
    private Long id;

    @Schema(description = "Название чата", example = "Рабочий чат")
    private String name;

    @Schema(description = "Тип чата", example = "group")
    private ChatType type;

    @Schema(description = "URL аватара", example = "https://example.com/chat.jpg")
    private String avatarUrl;

    @Schema(description = "Создатель")
    private UserDTO createdBy;

    @Schema(description = "Дата создания", example = "2026-03-08T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления", example = "2026-03-08T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Время последнего сообщения")
    private LocalDateTime lastMessageTime;
}