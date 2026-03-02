package org.example.dto.Reaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о реакции")
public class ReactionResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "Информация о сообщении")
    private MessageInfo message;

    @Schema(description = "Информация о пользователе")
    private UserInfo user;

    @Schema(description = "Эмодзи", example = "👍")
    private String emoji;

    @Schema(description = "Дата создания", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime createdAt;

    @Data
    @Schema(description = "Краткая информация о сообщении")
    public static class MessageInfo {
        private Long id;
    }

    @Data
    @Schema(description = "Краткая информация о пользователе")
    public static class UserInfo {
        private Long id;
        private String username;
    }
}