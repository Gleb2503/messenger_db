package org.example.dto.MessageRead;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageReadDTO {
    private Long id;
    private Long messageId;
    private Long userId;
    private LocalDateTime readAt;
}