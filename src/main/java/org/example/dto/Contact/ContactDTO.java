package org.example.dto.Contact;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContactDTO {
    private Long id;
    private Long userId;
    private Long contactUserId;
    private String nickname;
    private Boolean isBlocked;
    private LocalDateTime createdAt;
}