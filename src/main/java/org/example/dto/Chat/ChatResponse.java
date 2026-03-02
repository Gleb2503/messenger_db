package org.example.dto.Chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о чате")
public class ChatResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "Название чата", example = "Рабочий чат команды")
    private String name;

    @Schema(description = "Тип чата", example = "group")
    private String type;

    @Schema(description = "URL аватара", example = "https://example.com/chat-avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Создатель чата")
    private UserInfo createdBy;

    @Schema(description = "Дата создания", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime createdAt;

    @Schema(description = "Время последнего сообщения", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime lastMessageTime;

    @Data
    @Schema(description = "Краткая информация о пользователе")
    public static class UserInfo {
        private Long id;
        private String username;
        private String displayName;
    }
}