package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Информация о вложении")
public class AttachmentResponse {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "ID сообщения")
    private Long messageId;

    @Schema(description = "URL файла", example = "https://storage.example.com/files/document.pdf")
    private String fileUrl;

    @Schema(description = "Имя файла", example = "document.pdf")
    private String fileName;

    @Schema(description = "Размер файла в байтах", example = "2048576")
    private Long fileSize;

    @Schema(description = "MIME тип файла", example = "application/pdf")
    private String fileType;

    @Schema(description = "URL миниатюры", example = "https://storage.example.com/thumbs/document.jpg")
    private String thumbnailUrl;

    @Schema(description = "Дата загрузки", example = "2026-02-28T08:49:11.243Z")
    private LocalDateTime createdAt;
}