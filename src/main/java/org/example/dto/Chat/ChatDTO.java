package org.example.dto.Chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Информация о чате")
public class ChatDTO {

    @Schema(description = "ID чата", example = "1")
    private Long id;

    @Schema(description = "Название чата", example = "Рабочий чат")
    private String name;

    public ChatDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}