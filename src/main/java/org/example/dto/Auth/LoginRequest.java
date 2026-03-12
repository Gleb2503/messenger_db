package org.example.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на вход")
public class LoginRequest {

    @NotBlank(message = "Username не может быть пустым")
    @Schema(description = "Имя пользователя", example = "ivan_dev", required = true)
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "Пароль", example = "securePassword123", required = true)
    private String password;
}