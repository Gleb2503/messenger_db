package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String type;
    private String title;
    private String body;
    private Boolean isRead;
    private LocalDateTime createdAt;
}