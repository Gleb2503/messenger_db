package org.example.dto.Message;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private Long chatId;
    private Long senderId;
    private String content;
    private String messageType;
    private Boolean isEdited;
    private Boolean isDeleted;
    private String deliveryStatus;
    private LocalDateTime createdAt;
}