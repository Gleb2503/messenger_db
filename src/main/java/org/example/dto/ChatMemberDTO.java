package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMemberDTO {
    private Long id;
    private Long chatId;
    private Long userId;
    private String role;
    private Boolean isActive;
    private LocalDateTime joinedAt;
}