package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о контакте")
public class ContactResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "Информация о пользователе")
    private UserInfo user;

    @Schema(description = "Информация о контактируемом")
    private UserInfo contactUser;

    @Schema(description = "Псевдоним", example = "Маша Дизайн")
    private String nickname;

    @Schema(description = "Заблокирован ли", example = "false")
    private Boolean isBlocked;

    @Schema(description = "Дата добавления", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime createdAt;

    @Data
    @Schema(description = "Краткая информация о пользователе")
    public static class UserInfo {
        private Long id;
        private String username;
        private String displayName;
    }
}