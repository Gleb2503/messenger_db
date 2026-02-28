package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReactionDTO {
    private Long id;
    private Long messageId;
    private Long userId;
    private String emoji;
    private LocalDateTime createdAt;
}