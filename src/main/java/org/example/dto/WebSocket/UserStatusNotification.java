package org.example.dto.WebSocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusNotification {
    private Long userId;
    private String status;
    private LocalDateTime timestamp;
}