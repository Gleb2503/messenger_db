package org.example.dto.Attachment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.dto.Message.MessageDTO;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с информацией о вложении")
public class AttachmentResponse {

    @Schema(description = "ID вложения", example = "1")
    private Long id;

    @Schema(description = "Сообщение")
    private MessageDTO message;

    @Schema(description = "URL файла", example = "https://storage.example.com/files/document.pdf")
    private String fileUrl;

    @Schema(description = "Имя файла", example = "document.pdf")
    private String fileName;

    @Schema(description = "Размер файла", example = "2048576")
    private Long fileSize;

    @Schema(description = "Тип файла", example = "application/pdf")
    private String fileType;

    @Schema(description = "URL миниатюры", example = "https://storage.example.com/thumbs/document.jpg")
    private String thumbnailUrl;

    @Schema(description = "Дата создания", example = "2026-03-03T14:00:00")
    private LocalDateTime createdAt;
}