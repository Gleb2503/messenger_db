package org.example.dto.ChatMember;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.entity.ChatMember;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.enums.MemberRole;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на добавление участника в чат")
public class CreateChatMemberRequest {

    @NotNull(message = "chatId не может быть null")
    @Schema(description = "ID чата", example = "1", required = true)
    private Long chatId;

    @NotNull(message = "userId не может быть null")
    @Schema(description = "ID пользователя", example = "2", required = true)
    private Long userId;

    @Pattern(regexp = "^(admin|moderator|member)$", message = "Некорректная роль")
    @Schema(description = "Роль участника", example = "member")
    private String role;

    @Schema(description = "Заглушен ли чат", example = "false")
    private Boolean isMuted;

    @Schema(description = "Закреплён ли чат", example = "false")
    private Boolean isPinned;

    public ChatMember toEntity() {
        ChatMember member = new ChatMember();

        if (this.chatId != null) {
            Chat chat = new Chat();
            chat.setId(this.chatId);
            member.setChat(chat);
        }

        if (this.userId != null) {
            User user = new User();
            user.setId(this.userId);
            member.setUser(user);
        }

        if (this.role != null) {
            member.setRole(MemberRole.valueOf(this.role));
        } else {
            member.setRole(MemberRole.member);
        }

        member.setIsActive(true);
        member.setJoinedAt(LocalDateTime.now());
        member.setIsMuted(this.isMuted != null ? this.isMuted : false);
        member.setIsPinned(this.isPinned != null ? this.isPinned : false);

        return member;
    }
}