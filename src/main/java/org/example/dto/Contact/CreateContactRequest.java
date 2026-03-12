package org.example.dto.Contact;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;  // ✅ Импорт должен быть!
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.entity.Contact;
import org.example.entity.User;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на добавление контакта")
public class CreateContactRequest {

    @NotNull(message = "userId не может быть null")
    @Schema(description = "ID пользователя", example = "1", required = true)
    private Long userId;

    @NotNull(message = "contactUserId не может быть null")
    @Schema(description = "ID контакта", example = "2", required = true)
    private Long contactUserId;

    @Size(max = 100, message = "nickname не может превышать 100 символов")
    @Schema(description = "Псевдоним", example = "Друг")
    private String nickname;

    public Contact toEntity() {
        Contact contact = new Contact();

        if (this.userId != null) {
            User user = new User();
            user.setId(this.userId);
            contact.setUser(user);
        }

        if (this.contactUserId != null) {
            User contactUser = new User();
            contactUser.setId(this.contactUserId);
            contact.setContactUser(contactUser);
        }

        contact.setNickname(this.nickname);
        contact.setIsBlocked(false);
        contact.setCreatedAt(LocalDateTime.now());

        return contact;
    }
}