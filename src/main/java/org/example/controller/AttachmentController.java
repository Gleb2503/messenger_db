package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Attachment.AttachmentResponse;
import org.example.entity.Attachment;
import org.example.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Attachments", description = "Управление вложениями")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/chat/{chatId}/recent")
    @Operation(summary = "Получить последние вложения чата")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список вложений"),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    public ResponseEntity<List<AttachmentResponse>> getRecentAttachments(
            @Parameter(description = "ID чата", required = true, example = "1")
            @PathVariable Long chatId,

            @Parameter(description = "Количество вложений (макс. 500)", example = "500")
            @RequestParam(defaultValue = "500") int limit) {

        if (limit > 500) {
            limit = 500;
        }

        List<Attachment> attachments = attachmentService.getRecentAttachmentsByChatId(chatId, limit);

        List<AttachmentResponse> response = attachments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chats/last")
    @Operation(summary = "Получить последнее вложение для каждого чата")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список последних вложений")
    })
    public ResponseEntity<List<AttachmentResponse>> getLastAttachmentsPerChat() {
        List<Attachment> attachments = attachmentService.getLastAttachmentsPerChat();

        List<AttachmentResponse> response = attachments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/message/{messageId}")
    @Operation(summary = "Получить вложения сообщения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<AttachmentResponse>> getAttachmentsByMessage(
            @Parameter(description = "ID сообщения", required = true, example = "1")
            @PathVariable Long messageId) {
        List<Attachment> attachments = attachmentService.getAttachmentsByMessageId(messageId);
        List<AttachmentResponse> response = attachments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить вложение по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вложение найдено"),
            @ApiResponse(responseCode = "404", description = "Вложение не найдено")
    })
    public ResponseEntity<AttachmentResponse> getAttachmentById(
            @Parameter(description = "ID вложения", required = true, example = "1")
            @PathVariable Long id) {
        Attachment attachment = attachmentService.getAttachmentById(id);
        return ResponseEntity.ok(convertToResponse(attachment));
    }

    @PostMapping
    @Operation(summary = "Загрузить вложение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Вложение загружено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<AttachmentResponse> uploadAttachment(
            @Parameter(description = "Данные вложения", required = true)
            @RequestBody org.example.dto.Attachment.CreateAttachmentRequest request) {
        Attachment attachment = attachmentService.uploadAttachment(request.toEntity());
        return ResponseEntity.status(201).body(convertToResponse(attachment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить вложение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Вложение удалено"),
            @ApiResponse(responseCode = "404", description = "Вложение не найдено")
    })
    public ResponseEntity<Void> deleteAttachment(
            @Parameter(description = "ID вложения", required = true, example = "1")
            @PathVariable Long id) {
        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }

    private AttachmentResponse convertToResponse(Attachment attachment) {
        AttachmentResponse response = new AttachmentResponse();
        response.setId(attachment.getId());
        response.setFileUrl(attachment.getFileUrl());
        response.setFileName(attachment.getFileName());
        response.setFileSize(attachment.getFileSize());
        response.setFileType(attachment.getFileType());
        response.setThumbnailUrl(attachment.getThumbnailUrl());
        response.setCreatedAt(attachment.getCreatedAt());

        if (attachment.getMessage() != null) {
            response.setMessage(new org.example.dto.Message.MessageDTO(
                    attachment.getMessage().getId(),
                    attachment.getMessage().getContent()
            ));
        }

        return response;
    }
}