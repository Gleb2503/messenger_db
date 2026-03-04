package org.example.dto.Reaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.Reaction;
import org.example.entity.Message;
import org.example.entity.User;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на добавление реакции")
public class CreateReactionRequest {

    @Schema(description = "ID сообщения", example = "1")
    private Long messageId;

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "Эмодзи реакции", example = "👍")
    private String emoji;

    public Reaction toEntity() {
        Reaction reaction = new Reaction();

        if (this.messageId != null) {
            Message message = new Message();
            message.setId(this.messageId);
            reaction.setMessage(message);
        }

        if (this.userId != null) {
            User user = new User();
            user.setId(this.userId);
            reaction.setUser(user);
        }

        reaction.setEmoji(this.emoji != null ? this.emoji : "👍");
        reaction.setCreatedAt(LocalDateTime.now());

        return reaction;
    }
}