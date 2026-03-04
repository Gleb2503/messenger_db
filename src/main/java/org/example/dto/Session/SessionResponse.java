package org.example.dto.Session;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.dto.User.UserDTO;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с информацией о сессии")
public class SessionResponse {

    @Schema(description = "ID сессии", example = "1")
    private Long id;

    @Schema(description = "Пользователь")
    private UserDTO user;

    @Schema(description = "Имя устройства", example = "Chrome on Windows")
    private String deviceName;

    @Schema(description = "IP адрес", example = "192.168.1.100")
    private String ipAddress;

    @Schema(description = "User Agent", example = "Mozilla/5.0...")
    private String userAgent;

    @Schema(description = "Активна ли сессия", example = "true")
    private Boolean isActive;

    @Schema(description = "Дата создания", example = "2026-03-03T14:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Последняя активность", example = "2026-03-03T15:00:00")
    private LocalDateTime lastActiveAt;

    @Schema(description = "Дата истечения", example = "2026-03-10T14:00:00")
    private LocalDateTime expiresAt;
}