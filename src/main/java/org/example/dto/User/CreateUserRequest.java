package org.example.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.entity.User;
import org.example.enums.UserStatus;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание пользователя")
public class CreateUserRequest {

    @NotBlank(message = "Username не может быть пустым")
    @Size(min = 3, max = 50, message = "Username должен быть от 3 до 50 символов")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username может содержать только буквы, цифры и _")
    @Schema(description = "Имя пользователя", example = "ivan_dev", required = true)
    private String username;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат Email")
    @Size(max = 100, message = "Email не может превышать 100 символов")
    @Schema(description = "Email", example = "ivan@example.com", required = true)
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 100, message = "Пароль должен быть от 6 до 100 символов")
    @Schema(description = "Пароль", example = "securePassword123", required = true)
    private String password;

    @Size(max = 100, message = "DisplayName не может превышать 100 символов")
    @Schema(description = "Отображаемое имя", example = "Иван Иванов")
    private String displayName;

    @Pattern(regexp = "^\\+?[0-9\\s\\-\\(\\)]+$", message = "Некорректный формат телефона")
    @Size(max = 20, message = "PhoneNumber не может превышать 20 символов")
    @Schema(description = "Номер телефона", example = "+7 (999) 123-45-67")
    private String phoneNumber;

    @Size(max = 255, message = "AvatarUrl не может превышать 255 символов")
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