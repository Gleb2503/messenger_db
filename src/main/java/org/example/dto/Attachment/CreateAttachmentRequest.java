package org.example.dto.Attachment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.entity.Attachment;
import org.example.entity.Message;

@Data
@Schema(description = "Запрос на создание вложения")
public class CreateAttachmentRequest {

    @NotNull(message = "messageId не может быть null")
    @Schema(description = "ID сообщения", example = "1", required = true)
    private Long messageId;

    @NotBlank(message = "fileName не может быть пустым")
    @Size(max = 255, message = "fileName не может превышать 255 символов")
    @Schema(description = "Имя файла", example = "document.pdf", required = true)
    private String fileName;

    @NotNull(message = "fileSize не может быть null")
    @Schema(description = "Размер файла в байтах", example = "2048576", required = true)
    private Long fileSize;

    @Size(max = 100, message = "fileType не может превышать 100 символов")
    @Schema(description = "MIME тип файла", example = "application/pdf")
    private String fileType;

    @Size(max = 500, message = "thumbnailUrl не может превышать 500 символов")
    @Schema(description = "URL превью", example = "https://storage.example.com/thumbs/document.jpg")
    private String thumbnailUrl;

    public Attachment toEntity() {
        Attachment attachment = new Attachment();

        if (this.messageId != null) {
            Message message = new Message();
            message.setId(this.messageId);
            attachment.setMessage(message);
        }

        attachment.setFileName(this.fileName.trim());
        attachment.setFileSize(this.fileSize);
        attachment.setFileType(this.fileType != null ? this.fileType.trim() : null);
        attachment.setThumbnailUrl(this.thumbnailUrl != null ? this.thumbnailUrl.trim() : null);

        return attachment;
    }
}