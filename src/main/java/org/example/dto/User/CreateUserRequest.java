package org.example.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.User;
import org.example.enums.UserStatus;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание пользователя")
public class CreateUserRequest {

    @Schema(description = "Имя пользователя", example = "ivan_dev", required = true)
    private String username;

    @Schema(description = "Email", example = "ivan@example.com", required = true)
    private String email;

    @Schema(description = "Пароль", example = "securePassword123", required = true)
    private String password;

    @Schema(description = "Отображаемое имя", example = "Иван Иванов")
    private String displayName;

    @Schema(description = "Номер телефона", example = "+7 (999) 123-45-67")
    private String phoneNumber;

    @Schema(description = "URL аватара", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    public User toEntity() {
        User user = new User();
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setDisplayName(this.displayName);
        user.setPhoneNumber(this.phoneNumber);
        user.setAvatarUrl(this.avatarUrl);
        user.setStatus(UserStatus.offline);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}