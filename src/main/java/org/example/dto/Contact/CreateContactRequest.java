package org.example.dto.Contact;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.Contact;
import org.example.entity.User;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на добавление контакта")
public class CreateContactRequest {

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "ID контактного пользователя", example = "2")
    private Long contactUserId;

    @Schema(description = "Псевдоним", example = "Маша")
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