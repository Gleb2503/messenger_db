package org.example.dto.User;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String displayName;
    private String avatarUrl;
    private String status;
    private LocalDateTime lastSeen;
    private LocalDateTime createdAt;
}