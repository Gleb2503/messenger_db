package org.example.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление пользователя")
public class UpdateUserRequest {

    @Schema(description = "Отображаемое имя", example = "Иван Иванов")
    private String displayName;

    @Schema(description = "Номер телефона", example = "+7 (999) 123-45-67")
    private String phoneNumber;

    @Schema(description = "URL аватара", example = "https://example.com/avatar.jpg")
    private String avatarUrl;
}