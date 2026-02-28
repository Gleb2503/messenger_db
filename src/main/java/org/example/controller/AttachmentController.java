package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.Attachment;
import org.example.service.AttachmentService;
import org.example.exeption.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Attachments", description = "Управление вложениями")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping
    @Operation(summary = "Получить все вложения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Attachment.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<Attachment>> getAllAttachments() {
        return ResponseEntity.ok(attachmentService.getAllAttachments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить вложение по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вложение найдено"),
            @ApiResponse(responseCode = "404", description = "Вложение не найдено")
    })
    public ResponseEntity<Attachment> getAttachmentById(
            @Parameter(description = "Уникальный идентификатор вложения", required = true, example = "1")
            @PathVariable Long id) {
        return attachmentService.getAttachmentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with id: " + id));
    }

    @GetMapping("/message/{messageId}")
    @Operation(summary = "Получить вложения сообщения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    [
                      {
                        "id": 1,
                        "message": {"id": 10},
                        "fileUrl": "https://storage.example.com/files/document.pdf",
                        "fileName": "document.pdf",
                        "fileSize": 2048576,
                        "fileType": "application/pdf",
                        "createdAt": "2026-02-27T17:40:00"
                      },
                      {
                        "id": 2,
                        "message": {"id": 10},
                        "fileUrl": "https://storage.example.com/files/image.jpg",
                        "fileName": "image.jpg",
                        "fileSize": 512000,
                        "fileType": "image/jpeg",
                        "createdAt": "2026-02-27T17:41:00"
                      }
                    ]
                    """))),
            @ApiResponse(responseCode = "404", description = "Сообщение не найдено")
    })
    public ResponseEntity<List<Attachment>> getAttachmentsByMessageId(
            @Parameter(description = "Уникальный идентификатор сообщения", required = true, example = "10")
            @PathVariable Long messageId) {
        return ResponseEntity.ok(attachmentService.getAttachmentsByMessageId(messageId));
    }

    @PostMapping
    @Operation(summary = "Загрузить вложение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Вложение успешно загружено",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Attachment.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "message": {"id": 10},
                      "fileUrl": "https://storage.example.com/files/document.pdf",
                      "fileName": "document.pdf",
                      "fileSize": 2048576,
                      "fileType": "application/pdf",
                      "createdAt": "2026-02-27T17:40:00"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Сообщение не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Attachment> uploadAttachment(
            @Parameter(description = "Данные для создания вложения", required = true)
            @RequestBody Attachment attachment) {
        return ResponseEntity.ok(attachmentService.uploadAttachment(attachment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить вложение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Вложение успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Вложение не найдено")
    })
    public ResponseEntity<Void> deleteAttachment(
            @Parameter(description = "Уникальный идентификатор вложения", required = true, example = "1")
            @PathVariable Long id) {
        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }
}