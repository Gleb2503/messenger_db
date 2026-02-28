package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание сообщения")
public class CreateMessageRequest {

    @Schema(description = "ID чата", example = "1")
    private Long chatId;

    @Schema(description = "ID отправителя", example = "1")
    private Long senderId;

    @Schema(description = "Текст сообщения", example = "Привет всем!")
    private String content;

    @Schema(description = "Тип сообщения", example = "text", allowableValues = {"text", "image", "video", "audio", "file"})
    private String messageType;

    @Schema(description = "ID сообщения для ответа", example = "5")
    private Long replyToId;
}