package org.example.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ с токенами аутентификации")
public class AuthResponse {

    @Schema(description = "JWT токен доступа", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "JWT токен обновления", example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...")
    private String refreshToken;

    @Schema(description = "Время жизни токена в секундах", example = "86400")
    private long expiresIn;

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "ivan_dev")
    private String username;

    @Schema(description = "Email пользователя", example = "ivan@example.com")
    private String email;

}