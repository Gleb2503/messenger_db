package org.example.dto.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на вход по номеру телефона")
public class LoginRequest {

    @JsonProperty("phone")
    @NotBlank(message = "Номер телефона не может быть пустым")
    @Schema(description = "Номер телефона", example = "+79991234567", required = true)
    private String phone;

    @JsonProperty("password")
    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "Пароль", example = "securePassword123", required = true)
    private String password;
}