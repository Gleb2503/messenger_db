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

    @NotBlank(message = "name не может быть пустым")
    @Size(max = 100, message = "name не может превышать 100 символов")
    @Schema(description = "Название чата", example = "Рабочий чат", required = true)
    private String name;

    @Pattern(regexp = "^(private|group|channel)$", message = "Некорректный тип чата")
    @Schema(description = "Тип чата", example = "group")
    private String type;

    @Size(max = 255, message = "avatarUrl не может превышать 255 символов")
    @Schema(description = "URL аватара", example = "https://example.com/chat.jpg")
    private String avatarUrl;

    public Chat toEntity() {
        Chat chat = new Chat();
        chat.setName(this.name);
        if (this.type != null) {
            chat.setType(ChatType.valueOf(this.type));
        }
        chat.setAvatarUrl(this.avatarUrl);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        return chat;
    }
}