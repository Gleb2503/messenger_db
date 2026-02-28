package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на добавление участника чата")
public class CreateChatMemberRequest {

    @Schema(description = "ID чата", example = "2")
    private Long chatId;

    @Schema(description = "ID пользователя", example = "5")
    private Long userId;

    @Schema(description = "Роль в чате", example = "member", allowableValues = {"owner", "admin", "member"})
    private String role;

    @Schema(description = "Активен ли участник", example = "true")
    private Boolean isActive;
}