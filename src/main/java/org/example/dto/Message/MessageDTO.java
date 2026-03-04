package org.example.dto.Message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о сообщении")
public class MessageDTO {

    @Schema(description = "ID сообщения", example = "1")
    private Long id;

    @Schema(description = "Содержимое", example = "Привет всем!")
    private String content;
}