package org.example.dto.Contact;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.dto.User.UserDTO;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с информацией о контакте")
public class ContactResponse {

    @Schema(description = "ID контакта", example = "1")
    private Long id;

    @Schema(description = "Пользователь")
    private UserDTO user;

    @Schema(description = "Контактный пользователь")
    private UserDTO contactUser;

    @Schema(description = "Псевдоним", example = "Маша Дизайн")
    private String nickname;

    @Schema(description = "Заблокирован ли контакт", example = "false")
    private Boolean isBlocked;

    @Schema(description = "Дата добавления", example = "2026-03-03T14:00:00")
    private LocalDateTime createdAt;
}