package org.example.dto.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusRequest {
    private Long userId;
    private Long chatId;
    private Boolean online;
    private Long timestamp;
}