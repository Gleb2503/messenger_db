package org.example.dto.UserSettings;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserSettingsDTO {
    private Long id;
    private Long userId;
    private Boolean pushEnabled;
    private Boolean emailEnabled;
    private Boolean soundEnabled;
    private String theme;
    private String language;
    private LocalDateTime updatedAt;
}