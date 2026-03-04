package org.example.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о пользователе")
public class UserDTO {

    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "ivan_dev")
    private String username;

    @Schema(description = "Email", example = "ivan@example.com")
    private String email;
}