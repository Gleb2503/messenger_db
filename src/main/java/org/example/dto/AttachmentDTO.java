package org.example.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttachmentDTO {
    private Long id;
    private Long messageId;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private LocalDateTime createdAt;
}