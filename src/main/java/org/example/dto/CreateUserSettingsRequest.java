package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание настроек пользователя")
public class CreateUserSettingsRequest {

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "Push-уведомления включены", example = "true")
    private Boolean pushEnabled;

    @Schema(description = "Email-уведомления включены", example = "false")
    private Boolean emailEnabled;

    @Schema(description = "Звуковые уведомления включены", example = "true")
    private Boolean soundEnabled;

    @Schema(description = "Тема интерфейса", example = "dark", allowableValues = {"light", "dark", "system"})
    private String theme;

    @Schema(description = "Язык интерфейса", example = "ru")
    private String language;
}