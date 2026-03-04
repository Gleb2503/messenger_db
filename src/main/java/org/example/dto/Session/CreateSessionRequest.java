package org.example.dto.Session;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.Session;
import org.example.entity.User;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание сессии")
public class CreateSessionRequest {

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "Имя устройства", example = "Chrome on Windows")
    private String deviceName;

    @Schema(description = "IP адрес", example = "192.168.1.100")
    private String ipAddress;

    @Schema(description = "User Agent", example = "Mozilla/5.0...")
    private String userAgent;

    public Session toEntity() {
        Session session = new Session();
        if (this.userId != null) {
            User user = new User();
            user.setId(this.userId);
            session.setUser(user);
        }
        session.setDeviceName(this.deviceName);
        session.setIpAddress(this.ipAddress);
        session.setUserAgent(this.userAgent);
        session.setIsActive(true);
        session.setCreatedAt(LocalDateTime.now());
        session.setLastActiveAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusDays(7));
        return session;
    }
}