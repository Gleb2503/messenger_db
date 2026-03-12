package org.example.dto.Reaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;  // ✅ Импорт должен быть!
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.entity.Reaction;
import org.example.entity.Message;
import org.example.entity.User;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на добавление реакции")
public class CreateReactionRequest {

    @NotNull(message = "messageId не может быть null")
    @Schema(description = "ID сообщения", example = "1", required = true)
    private Long messageId;

    @NotNull(message = "userId не может быть null")
    @Schema(description = "ID пользователя", example = "1", required = true)
    private Long userId;

    @NotBlank(message = "emoji не может быть пустым")
    @Size(max = 10, message = "emoji не может превышать 10 символов")
    @Schema(description = "Эмодзи реакции", example = "👍", required = true)
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

        reaction.setEmoji(this.emoji);
        reaction.setCreatedAt(LocalDateTime.now());

        return reaction;
    }
}