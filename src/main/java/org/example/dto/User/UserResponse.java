package org.example.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.enums.UserStatus;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с информацией о пользователе")
public class UserResponse {

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "ivan_dev")
    private String username;

    @Schema(description = "Email", example = "ivan@example.com")
    private String email;

    @Schema(description = "Отображаемое имя", example = "Иван Иванов")
    private String displayName;

    @Schema(description = "Номер телефона", example = "+7 (999) 123-45-67")
    private String phoneNumber;

    @Schema(description = "URL аватара", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Статус", example = "online")
    private UserStatus status;

    @Schema(description = "Дата создания", example = "2026-03-03T14:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Последний вход", example = "2026-03-03T15:00:00")
    private LocalDateTime lastSeen;
}