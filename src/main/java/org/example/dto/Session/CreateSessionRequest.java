package org.example.dto.Session;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.entity.Session;
import org.example.entity.User;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание сессии")
public class CreateSessionRequest {

    @NotNull(message = "userId не может быть null")
    @Schema(description = "ID пользователя", example = "1", required = true)
    private Long userId;

    @Size(max = 100, message = "deviceName не может превышать 100 символов")
    @Schema(description = "Имя устройства", example = "Chrome on Windows")
    private String deviceName;

    @Size(max = 50, message = "ipAddress не может превышать 50 символов")
    @Schema(description = "IP адрес", example = "192.168.1.1")
    private String ipAddress;

    @Size(max = 500, message = "userAgent не может превышать 500 символов")
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

        return session;
    }
}