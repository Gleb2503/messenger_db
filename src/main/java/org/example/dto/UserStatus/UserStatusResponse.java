package org.example.dto.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusResponse {
    private Long userId;
    private Boolean online;
    private Long timestamp;
}