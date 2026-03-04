package org.example.dto.UserSettings;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.dto.User.UserDTO;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с настройками пользователя")
public class UserSettingsResponse {

    @Schema(description = "ID настроек", example = "1")
    private Long id;

    @Schema(description = "Пользователь")
    private UserDTO user;

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

    @Schema(description = "Дата создания", example = "2026-03-03T14:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления", example = "2026-03-03T14:00:00")
    private LocalDateTime updatedAt;
}