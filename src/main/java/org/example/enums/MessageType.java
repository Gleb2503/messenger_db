package org.example.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип сообщения")
public enum MessageType {

    @Schema(description = "Текстовое сообщение")
    text,

    @Schema(description = "Изображение")
    image,

    @Schema(description = "Видео")
    video,

    @Schema(description = "Аудио")
    audio,

    @Schema(description = "Файл")
    file
}