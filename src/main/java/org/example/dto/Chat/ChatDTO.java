package org.example.dto.Chat;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatDTO {
    private Long id;
    private String name;
    private String type;
    private String avatarUrl;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastMessageTime;
}