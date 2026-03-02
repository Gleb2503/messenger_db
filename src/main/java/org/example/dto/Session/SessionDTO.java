package org.example.dto.Session;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SessionDTO {
    private Long id;
    private Long userId;
    private String deviceName;
    private String deviceType;
    private String ipAddress;
    private Boolean isActive;
    private LocalDateTime lastActiveAt;
    private LocalDateTime expiresAt;
}