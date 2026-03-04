package org.example.dto.UserSettings;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.UserSettings;
import org.example.entity.User;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание настроек")
public class CreateUserSettingsRequest {

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "Push-уведомления", example = "true")
    private Boolean pushEnabled;

    @Schema(description = "Email-уведомления", example = "true")
    private Boolean emailEnabled;

    @Schema(description = "Звуковые уведомления", example = "true")
    private Boolean soundEnabled;

    @Schema(description = "Тема", example = "dark")
    private String theme;

    @Schema(description = "Язык", example = "ru")
    private String language;

    public UserSettings toEntity() {
        UserSettings settings = new UserSettings();
        if (this.userId != null) {
            User user = new User();
            user.setId(this.userId);
            settings.setUser(user);
        }
        settings.setPushEnabled(this.pushEnabled != null ? this.pushEnabled : true);
        settings.setEmailEnabled(this.emailEnabled != null ? this.emailEnabled : true);
        settings.setSoundEnabled(this.soundEnabled != null ? this.soundEnabled : true);
        settings.setTheme(this.theme != null ? this.theme : "system");
        settings.setLanguage(this.language != null ? this.language : "ru");
        settings.setCreatedAt(LocalDateTime.now());
        settings.setUpdatedAt(LocalDateTime.now());
        return settings;
    }
}