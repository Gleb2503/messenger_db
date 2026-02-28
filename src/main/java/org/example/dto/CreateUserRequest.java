package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание пользователя")
public class CreateUserRequest {

    @Schema(description = "Имя пользователя", example = "ivan_dev")
    private String username;

    @Schema(description = "Email адрес", example = "ivan@example.com")
    private String email;

    @Schema(description = "Пароль", example = "securePassword123")
    private String password;

    @Schema(description = "Номер телефона", example = "+79001234567")
    private String phoneNumber;

    @Schema(description = "Отображаемое имя", example = "Иван Иванов")
    private String displayName;

    @Schema(description = "Статус пользователя", example = "online",
            allowableValues = {"online", "offline", "away", "busy"})
    private String status;
}