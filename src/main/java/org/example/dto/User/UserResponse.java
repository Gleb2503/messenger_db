package org.example.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о пользователе")
public class UserResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "ivan_dev")
    private String username;

    @Schema(description = "Email адрес", example = "ivan@example.com")
    private String email;

    @Schema(description = "Отображаемое имя", example = "Иван Иванов")
    private String displayName;

    @Schema(description = "URL аватара", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Статус", example = "online")
    private String status;

    @Schema(description = "Последнее время в сети", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime lastSeen;

    @Schema(description = "Дата регистрации", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime createdAt;
}