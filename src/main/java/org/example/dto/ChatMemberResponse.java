package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация об участнике чата")
public class ChatMemberResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "Информация о чате")
    private ChatInfo chat;

    @Schema(description = "Информация о пользователе")
    private UserInfo user;

    @Schema(description = "Роль в чате", example = "member")
    private String role;

    @Schema(description = "Активен ли", example = "true")
    private Boolean isActive;

    @Schema(description = "Дата вступления", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime joinedAt;

    @Schema(description = "Дата выхода", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime leftAt;

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