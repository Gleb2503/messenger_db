package org.example.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ с токенами аутентификации")
public class AuthResponse {

    @Schema(description = "JWT токен доступа")
    private String token;

    @Schema(description = "JWT токен обновления")
    private String refreshToken;

    @Schema(description = "Персональный API ключ")
    private String apiKey;

    @Schema(description = "Время жизни токена в секундах")
    private long expiresIn;

    @Schema(description = "ID пользователя")
    private Long id;

    @Schema(description = "Имя пользователя")
    private String username;

    @Schema(description = "Email пользователя")
    private String email;
}