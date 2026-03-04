package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Chat.CreateChatRequest;
import org.example.dto.Chat.ChatResponse;
import org.example.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Chats", description = "Управление чатами")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    @Operation(summary = "Получить последние 100 чатов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<ChatResponse>> getLast100Chats() {
        List<ChatResponse> response = chatService.getLast100Chats();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить чат по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Чат найден"),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    public ResponseEntity<ChatResponse> getChatById(
            @Parameter(description = "ID чата", required = true, example = "1")
            @PathVariable Long id) {
        ChatResponse response = chatService.getChatById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/created-by/{userId}")
    @Operation(summary = "Получить последние 100 чатов созданных пользователем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<ChatResponse>> getLast100ChatsByCreator(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        List<ChatResponse> response = chatService.getLast100ChatsByCreator(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Создать новый чат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Чат создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<ChatResponse> createChat(
            @Parameter(description = "Данные чата", required = true)
            @RequestBody CreateChatRequest request) {
        ChatResponse response = chatService.createChat(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить чат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Чат обновлён"),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    public ResponseEntity<ChatResponse> updateChat(
            @Parameter(description = "ID чата", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Обновлённые данные", required = true)
            @RequestBody CreateChatRequest request) {
        ChatResponse response = chatService.updateChat(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Чат удалён"),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    public ResponseEntity<Void> deleteChat(
            @Parameter(description = "ID чата", required = true, example = "1")
            @PathVariable Long id) {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }
}