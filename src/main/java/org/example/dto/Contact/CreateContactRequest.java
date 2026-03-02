package org.example.dto.Contact;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на добавление контакта")
public class CreateContactRequest {

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "ID контактируемого пользователя", example = "2")
    private Long contactUserId;

    @Schema(description = "Псевдоним контакта", example = "Маша Дизайн")
    private String nickname;
}