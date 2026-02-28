package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание чата")
public class CreateChatRequest {

    @Schema(description = "Название чата", example = "Рабочий чат команды")
    private String name;

    @Schema(description = "Тип чата", example = "group", allowableValues = {"private", "group", "channel"})
    private String type;

    @Schema(description = "ID создателя", example = "1")
    private Long createdById;

    @Schema(description = "URL аватара чата", example = "https://example.com/chat-avatar.jpg")
    private String avatarUrl;
}