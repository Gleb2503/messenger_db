package org.example.dto.WebSocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageReadRequest {
    private Long messageId;
    private Long userId;
    private LocalDateTime readAt;
}