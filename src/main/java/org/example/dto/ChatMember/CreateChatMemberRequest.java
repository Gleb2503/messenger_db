package org.example.dto.ChatMember;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.ChatMember;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.enums.MemberRole;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на добавление участника в чат")
public class CreateChatMemberRequest {

    @Schema(description = "ID чата", example = "1")
    private Long chatId;

    @Schema(description = "ID пользователя", example = "2")
    private Long userId;

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