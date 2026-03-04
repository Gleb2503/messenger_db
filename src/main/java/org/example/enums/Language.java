package org.example.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Язык интерфейса")
public enum Language {

    @Schema(description = "Русский")
    ru,

    @Schema(description = "Английский")
    en,

    @Schema(description = "Испанский")
    es,

    @Schema(description = "Немецкий")
    de,

    @Schema(description = "Французский")
    fr,

    @Schema(description = "Китайский")
    zh
}