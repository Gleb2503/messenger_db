package org.example.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус доставки сообщения")
public enum DeliveryStatus {

    @Schema(description = "Отправлено")
    sent,

    @Schema(description = "Доставлено")
    delivered,

    @Schema(description = "Прочитано")
    read
}