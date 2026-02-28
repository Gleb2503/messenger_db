package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на загрузку вложения")
public class CreateAttachmentRequest {

    @Schema(description = "ID сообщения", example = "10")
    private Long messageId;

    @Schema(description = "URL файла", example = "https://storage.example.com/files/document.pdf")
    private String fileUrl;

    @Schema(description = "Имя файла", example = "document.pdf")
    private String fileName;

    @Schema(description = "Размер файла в байтах", example = "2048576")
    private Long fileSize;

    @Schema(description = "MIME тип файла", example = "application/pdf")
    private String fileType;
}