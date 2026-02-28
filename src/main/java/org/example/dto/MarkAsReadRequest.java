package org.example.dto;

import lombok.Data;

@Data
public class MarkAsReadRequest {
    private Long messageId;
    private Long userId;
}