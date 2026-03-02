package org.example.dto.UserSettings;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о настройках пользователя")
public class UserSettingsResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "ID пользователя")
    private Long userId;

    @Schema(description = "Push-уведомления включены", example = "true")
    private Boolean pushEnabled;

    @Schema(description = "Email-уведомления включены", example = "false")
    private Boolean emailEnabled;

    @Schema(description = "Звуковые уведомления включены", example = "true")
    private Boolean soundEnabled;

    @Schema(description = "Тема интерфейса", example = "dark")
    private String theme;

    @Schema(description = "Язык интерфейса", example = "ru")
    private String language;

    @Schema(description = "Дата создания", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime updatedAt;
}