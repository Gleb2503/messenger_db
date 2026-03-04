package org.example.dto.ChatMember;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.dto.Chat.ChatDTO;
import org.example.dto.User.UserDTO;
import org.example.enums.MemberRole;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с информацией об участнике чата")
public class ChatMemberResponse {

    @Schema(description = "ID участника", example = "1")
    private Long id;

    @Schema(description = "Чат")
    private ChatDTO chat;

    @Schema(description = "Пользователь")
    private UserDTO user;

    @Schema(description = "Роль участника", example = "member")
    private MemberRole role;

    @Schema(description = "Активен ли участник", example = "true")
    private Boolean isActive;

    @Schema(description = "Дата вступления", example = "2026-03-03T14:00:00")
    private LocalDateTime joinedAt;

    @Schema(description = "Дата выхода")
    private LocalDateTime leftAt;

    @Schema(description = "Заглушен ли чат", example = "false")
    private Boolean isMuted;

    @Schema(description = "Закреплён ли чат", example = "true")
    private Boolean isPinned;
}