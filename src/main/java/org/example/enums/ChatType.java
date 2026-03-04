package org.example.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип чата")
public enum ChatType {

    @Schema(description = "Личный чат (1 на 1)")
    private_chat,

    @Schema(description = "Групповой чат")
    group,

    @Schema(description = "Канал (только админы пишут)")
    channel
}