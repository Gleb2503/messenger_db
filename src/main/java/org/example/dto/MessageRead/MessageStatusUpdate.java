package org.example.dto.MessageRead;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageStatusUpdate {
    private Long messageId;
    private String status;
    private Long chatId;
    private Long updatedAt;
}