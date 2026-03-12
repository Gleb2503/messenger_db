package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Chat.CreateChatRequest;
import org.example.dto.Chat.ChatResponse;
import org.example.service.ChatService;
import org.example.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Chats", description = "Управление чатами")
public class ChatController {

    private final ChatService chatService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getLast100Chats() {
        List<ChatResponse> response = chatService.getLast100Chats();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatResponse> getChatById(@PathVariable Long id) {
        ChatResponse response = chatService.getChatById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/created-by/{userId}")
    public ResponseEntity<List<ChatResponse>> getLast100ChatsByCreator(@PathVariable Long userId) {
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
            @Valid @RequestBody CreateChatRequest request,
            HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        Long currentUserId = jwtTokenProvider.getUserIdFromToken(token);
        if (currentUserId == null) {
            return ResponseEntity.status(401).build();
        }

        ChatResponse response = chatService.createChat(request, currentUserId);
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
            @Valid @RequestBody CreateChatRequest request) {
        ChatResponse response = chatService.updateChat(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }
}