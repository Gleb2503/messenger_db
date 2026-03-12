package org.example.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Ответ аутентификации")
public class AuthResponse {

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "Имя пользователя", example = "ivan_dev")
    private String username;

    @Schema(description = "Access токен")
    private String token;

    @Schema(description = "Refresh токен")
    private String refreshToken;
}