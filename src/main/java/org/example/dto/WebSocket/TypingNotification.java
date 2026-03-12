package org.example.dto.WebSocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypingNotification {
    private Long chatId;
    private Long userId;
    private String username;
    private boolean isTyping;
    private LocalDateTime timestamp;
}