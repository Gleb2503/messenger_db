package org.example.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление пользователя")
public class UpdateUserRequest {

    @Size(max = 100, message = "DisplayName не может превышать 100 символов")
    @Schema(description = "Отображаемое имя", example = "Иван Иванов")
    private String displayName;

    @Pattern(regexp = "^\\+?[0-9\\s\\-\\(\\)]+$", message = "Некорректный формат телефона")
    @Size(max = 20, message = "PhoneNumber не может превышать 20 символов")
    @Schema(description = "Номер телефона", example = "+7 (999) 123-45-67")
    private String phoneNumber;

    @Size(max = 255, message = "AvatarUrl не может превышать 255 символов")
    @Schema(description = "URL аватара", example = "https://example.com/avatar.jpg")
    private String avatarUrl;
}