package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Attachment.AttachmentResponse;
import org.example.dto.Attachment.CreateAttachmentRequest;
import org.example.service.AttachmentService;
import org.example.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Attachments", description = "Управление вложениями")
@Slf4j
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity<List<AttachmentResponse>> getLast100Attachments() {
        log.debug("Getting last 100 attachments");
        return ResponseEntity.ok(attachmentService.getLast100Attachments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttachmentResponse> getAttachmentById(@PathVariable Long id) {
        log.debug("Getting attachment by id: {}", id);
        return ResponseEntity.ok(attachmentService.getAttachmentById(id));
    }

    @GetMapping("/message/{messageId}")
    public ResponseEntity<List<AttachmentResponse>> getLast100AttachmentsByMessage(@PathVariable Long messageId) {
        log.debug("Getting attachments for message: {}", messageId);
        return ResponseEntity.ok(attachmentService.getLast100AttachmentsByMessage(messageId));
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<AttachmentResponse>> getLast100AttachmentsByChat(@PathVariable Long chatId) {
        log.debug("Getting attachments for chat: {}", chatId);
        return ResponseEntity.ok(attachmentService.getLast100AttachmentsByChat(chatId));
    }

    @GetMapping("/recent-per-chat")
    public ResponseEntity<List<AttachmentResponse>> getLastAttachmentsPerChat() {
        log.debug("Getting last attachments per chat");
        return ResponseEntity.ok(attachmentService.getLastAttachmentsPerChat());
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Загрузить вложение в облако")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Файл успешно загружен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "401", description = "Неавторизован"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ResponseEntity<AttachmentResponse> createAttachment(

            @Parameter(description = "Данные вложения", required = true)
            @ModelAttribute @Valid CreateAttachmentRequest request,

            @Parameter(
                    description = "Файл для загрузки",
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestParam("file") MultipartFile file,

            HttpServletRequest httpRequest) {

        log.debug("Received attachment upload request: fileName={}", request.getFileName());


        String token = httpRequest.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header");
            return ResponseEntity.status(401).build();
        }

        Long userId;
        try {
            userId = jwtTokenProvider.getUserIdFromToken(token.substring(7));
        } catch (Exception e) {
            log.warn("Failed to extract userId from token", e);
            return ResponseEntity.status(401).build();
        }

        if (userId == null) {
            log.warn("userId is null after token parsing");
            return ResponseEntity.status(401).build();
        }

        log.info("Uploading attachment for userId={}", userId);

        AttachmentResponse response = attachmentService.createAttachment(request, file, userId);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        log.debug("Deleting attachment: id={}", id);
        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }
}