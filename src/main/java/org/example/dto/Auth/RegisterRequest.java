package org.example.dto.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.entity.User;
import org.example.enums.UserStatus;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на регистрацию")
public class RegisterRequest {

    @NotBlank(message = "Username не может быть пустым")
    @Size(min = 3, max = 50, message = "Username должен быть от 3 до 50 символов")
    @Schema(description = "Имя пользователя", example = "ivan_dev", required = true)
    private String username;  // ← Поле должно быть private

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат Email")
    @Schema(description = "Email", example = "ivan@example.com", required = true)
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 100, message = "Пароль должен быть от 6 до 100 символов")
    @Schema(description = "Пароль", example = "securePassword123", required = true)
    private String password;

    @Schema(description = "Отображаемое имя", example = "Иван Иванов")
    private String displayName;

    public User toEntity() {
        User user = new User();
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setDisplayName(this.displayName);
        user.setStatus(UserStatus.offline);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}