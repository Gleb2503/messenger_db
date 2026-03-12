package org.example.dto.Attachment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.Message.MessageResponse;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с данными вложения")
public class AttachmentResponse {

    @Schema(description = "ID вложения", example = "1")
    private Long id;

    @Schema(description = "URL файла", example = "https://storage.yandexcloud.net/bucket/file.pdf")
    private String fileUrl;

    @Schema(description = "Имя файла", example = "document.pdf")
    private String fileName;

    @Schema(description = "Размер файла в байтах", example = "2048576")
    private Long fileSize;

    @Schema(description = "MIME тип файла", example = "application/pdf")
    private String fileType;

    @Schema(description = "URL превью", example = "https://storage.yandexcloud.net/bucket/thumb.jpg")
    private String thumbnailUrl;

    @Schema(description = "Дата создания", example = "2026-03-09T16:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Сообщение")
    private MessageResponse message;
}