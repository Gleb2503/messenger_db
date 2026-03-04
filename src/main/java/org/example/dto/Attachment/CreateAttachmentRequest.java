package org.example.dto.Attachment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.entity.Attachment;
import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание вложения")
public class CreateAttachmentRequest {

    @Schema(description = "ID сообщения", example = "1")
    private Long messageId;

    @Schema(description = "URL файла", example = "https://storage.example.com/files/document.pdf")
    private String fileUrl;

    @Schema(description = "Имя файла", example = "document.pdf")
    private String fileName;

    @Schema(description = "Размер файла в байтах", example = "2048576")
    private Long fileSize;

    @Schema(description = "Тип файла", example = "application/pdf")
    private String fileType;

    @Schema(description = "URL миниатюры", example = "https://storage.example.com/thumbs/document.jpg")
    private String thumbnailUrl;

    public Attachment toEntity() {
        Attachment attachment = new Attachment();
        attachment.setFileUrl(this.fileUrl);
        attachment.setFileName(this.fileName);
        attachment.setFileSize(this.fileSize);
        attachment.setFileType(this.fileType);
        attachment.setThumbnailUrl(this.thumbnailUrl);
        attachment.setCreatedAt(LocalDateTime.now());
        return attachment;
    }
}