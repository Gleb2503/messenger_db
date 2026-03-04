package org.example.dto.Chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.enums.ChatType;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание чата")
public class CreateChatRequest {

    @Schema(description = "Название чата", example = "Рабочий чат")
    private String name;

    @Schema(description = "Тип чата", example = "group")
    private String type;

    @Schema(description = "URL аватара", example = "https://example.com/chat.jpg")
    private String avatarUrl;

    @Schema(description = "ID создателя", example = "1")
    private Long createdById;

    public Chat toEntity() {
        Chat chat = new Chat();
        chat.setName(this.name);
        if (this.type != null) {
            chat.setType(ChatType.valueOf(this.type));
        }
        chat.setAvatarUrl(this.avatarUrl);
        if (this.createdById != null) {
            User creator = new User();
            creator.setId(this.createdById);
            chat.setCreatedBy(creator);
        }
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        return chat;
    }
}