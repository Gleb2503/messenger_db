package org.example.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление токена")
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token не может быть пустым")
    @Schema(description = "Refresh токен", example = "eyJhbGciOiJIUzM4NCJ9...", required = true)
    private String refreshToken;
}