package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ChatMember.CreateChatMemberRequest;
import org.example.dto.ChatMember.ChatMemberResponse;
import org.example.service.ChatMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/chat-members")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "ChatMembers", description = "Участники чатов")
public class ChatMemberController {

    private final ChatMemberService chatMemberService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить последние 100 чатов пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<ChatMemberResponse>> getLast100ChatsByUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        List<ChatMemberResponse> response = chatMemberService.getLast100MembersByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/{chatId}")
    @Operation(summary = "Получить последних 100 участников чата")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<ChatMemberResponse>> getLast100MembersByChat(
            @Parameter(description = "ID чата", required = true, example = "1")
            @PathVariable Long chatId) {
        List<ChatMemberResponse> response = chatMemberService.getLast100MembersByChat(chatId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Добавить участника в чат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Участник добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<ChatMemberResponse> addMember(
            @Parameter(description = "Данные участника", required = true)
            @Valid @RequestBody CreateChatMemberRequest request) {

        ChatMemberResponse response = chatMemberService.addMember(request);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить участника из чата")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Участник удалён"),
            @ApiResponse(responseCode = "404", description = "Участник не найден")
    })
    public ResponseEntity<Void> removeMember(
            @Parameter(description = "ID участника", required = true, example = "1")
            @PathVariable Long id) {
        chatMemberService.removeMember(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Обновить роль участника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль обновлена"),
            @ApiResponse(responseCode = "404", description = "Участник не найден")
    })
    public ResponseEntity<ChatMemberResponse> updateRole(
            @Parameter(description = "ID участника", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Новая роль", required = true, example = "admin")
            @RequestParam String role) {
        ChatMemberResponse response = chatMemberService.updateRole(id, role);
        return ResponseEntity.ok(response);
    }
}