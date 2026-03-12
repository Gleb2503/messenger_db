package org.example.dto.Message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.User.UserDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о сообщении, на которое отвечаем")
public class ReplyToDTO {

    @Schema(description = "ID сообщения", example = "5")
    private Long id;

    @Schema(description = "Краткое содержимое", example = "Привет!")
    private String content;

    @Schema(description = "Отправитель оригинального сообщения")
    private UserDTO sender;
}