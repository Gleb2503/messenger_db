package org.example.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Роль участника чата")
public enum MemberRole {

    @Schema(description = "Владелец чата")
    owner,

    @Schema(description = "Администратор")
    admin,

    @Schema(description = "Обычный участник")
    member
}