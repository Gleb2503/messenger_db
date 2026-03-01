package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентификацию/регистрацию")
public class AuthRequest {

    @Schema(description = "Имя пользователя", example = "ivan_dev")
    private String username;

    @Schema(description = "Email адрес", example = "ivan@example.com")
    private String email;

    @Schema(description = "Пароль", example = "securePassword123")
    private String password;

    @Schema(description = "Отображаемое имя", example = "Иван Иванов")
    private String displayName;

    @Schema(description = "Номер телефона", example = "+79001234567")
    private String phoneNumber;
}