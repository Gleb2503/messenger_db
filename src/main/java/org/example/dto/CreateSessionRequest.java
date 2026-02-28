package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание сессии")
public class CreateSessionRequest {

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "Название устройства", example = "Chrome on Windows")
    private String deviceName;

    @Schema(description = "Тип устройства", example = "desktop", allowableValues = {"mobile", "desktop", "web"})
    private String deviceType;

    @Schema(description = "IP адрес", example = "192.168.1.100")
    private String ipAddress;

    @Schema(description = "User Agent", example = "Mozilla/5.0...")
    private String userAgent;

    @Schema(description = "Время истечения сессии", example = "2026-03-28T08:49:11.243Z")
    private LocalDateTime expiresAt;
}