package org.example.dto.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class RegisterRequest {

    @NotBlank(message = "Username не может быть пустым")
    @Size(min = 3, max = 50, message = "Username должен быть от 3 до 50 символов")
    @Schema(description = "Имя пользователя", example = "ivan_dev", required = true)
    private String username;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат Email")
    @Schema(description = "Email", example = "ivan@example.com", required = true)
    private String email;

    @JsonProperty("phone")
    @NotBlank(message = "Номер телефона не может быть пустым")
    @Size(min = 10, max = 20, message = "Номер телефона должен быть от 10 до 20 символов")
    @Schema(description = "Номер телефона", example = "+79991234567", required = true)
    private String phone;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 100, message = "Пароль должен быть от 6 до 100 символов")
    @Schema(description = "Пароль", example = "securePassword123", required = true)
    private String password;

    @Schema(description = "Отображаемое имя", example = "Иван Иванов")
    private String displayName;
}