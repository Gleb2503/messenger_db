package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о сессии")
public class SessionResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "ID пользователя")
    private Long userId;

    @Schema(description = "Название устройства", example = "Chrome on Windows")
    private String deviceName;

    @Schema(description = "Тип устройства", example = "desktop")
    private String deviceType;

    @Schema(description = "IP адрес", example = "192.168.1.100")
    private String ipAddress;

    @Schema(description = "Активна ли сессия", example = "true")
    private Boolean isActive;

    @Schema(description = "Последняя активность", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime lastActiveAt;

    @Schema(description = "Дата создания", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime createdAt;

    @Schema(description = "Время истечения", example = "2026-03-28T08:49:11.243Z")
    private LocalDateTime expiresAt;
}