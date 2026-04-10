package org.example.dto.Chat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.entity.Chat;
import org.example.enums.ChatType;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание чата")
public class CreateChatRequest {

    @Size(max = 100, message = "name не может превышать 100 символов")
    @Schema(description = "Название чата (для группового чата)", example = "Рабочий чат")
    private String name;

    @NotBlank(message = "type не может быть пустым")
    @Pattern(regexp = "^(private_chat|group|channel)$", message = "Некорректный тип чата")
    @Schema(description = "Тип чата", example = "private_chat", required = true)
    private String type;

    @Size(max = 255, message = "avatarUrl не может превышать 255 символов")
    @Schema(description = "URL аватара", example = "https://example.com/chat.jpg")
    private String avatarUrl;

    @Schema(description = "Телефон участника (для личного чата)", example = "+79991112233")
    private String participantPhone;

    public Chat toEntity() {
        Chat chat = new Chat();

        if ("group".equals(this.type) || "channel".equals(this.type)) {
            chat.setName(this.name);
        }

        if (this.type != null) {
            chat.setType(ChatType.valueOf(this.type));
        }
        chat.setAvatarUrl(this.avatarUrl);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        chat.setPinned(false);

        return chat;
    }
}