package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Message.CreateMessageRequest;
import org.example.dto.Message.MessageResponse;
import org.example.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Messages", description = "Управление сообщениями")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageResponse>> getLast100Messages() {
        List<MessageResponse> response = messageService.getLast100Messages();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getMessageById(@PathVariable Long id) {
        MessageResponse response = messageService.getMessageById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageResponse>> getLast100MessagesByChat(@PathVariable Long chatId) {
        List<MessageResponse> response = messageService.getLast100MessagesByChat(chatId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<MessageResponse>> getLast100MessagesBySender(@PathVariable Long senderId) {
        List<MessageResponse> response = messageService.getLast100MessagesBySender(senderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Отправить сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Сообщение отправлено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<MessageResponse> sendMessage(
            @Parameter(description = "Данные сообщения", required = true)
            @Valid @RequestBody CreateMessageRequest request) {
        MessageResponse response = messageService.sendMessage(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сообщение обновлено"),
            @ApiResponse(responseCode = "404", description = "Сообщение не найдено")
    })
    public ResponseEntity<MessageResponse> updateMessage(
            @Parameter(description = "ID сообщения", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Обновлённые данные", required = true)
            @Valid @RequestBody CreateMessageRequest request) {
        MessageResponse response = messageService.updateMessage(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}