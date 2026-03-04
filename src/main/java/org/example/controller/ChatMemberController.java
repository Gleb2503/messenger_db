package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ChatMember.CreateChatMemberRequest;
import org.example.dto.ChatMember.ChatMemberResponse;
import org.example.entity.ChatMember;
import org.example.service.ChatMemberService;
import org.example.exeption.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        List<ChatMember> members = chatMemberService.getLast100MembersByUser(userId);
        List<ChatMemberResponse> response = members.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
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
        List<ChatMember> members = chatMemberService.getLast100MembersByChat(chatId);
        List<ChatMemberResponse> response = members.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
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
            @RequestBody CreateChatMemberRequest request) {
        ChatMember member = chatMemberService.addMember(request.toEntity());
        return ResponseEntity.status(201).body(convertToResponse(member));
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
        ChatMember member = chatMemberService.updateRole(id, role);
        return ResponseEntity.ok(convertToResponse(member));
    }

    private ChatMemberResponse convertToResponse(ChatMember member) {
        ChatMemberResponse response = new ChatMemberResponse();
        response.setId(member.getId());
        response.setRole(member.getRole());
        response.setIsActive(member.getIsActive());
        response.setJoinedAt(member.getJoinedAt());
        response.setLeftAt(member.getLeftAt());
        response.setIsMuted(member.getIsMuted());
        response.setIsPinned(member.getIsPinned());

        if (member.getChat() != null) {
            response.setChat(new org.example.dto.Chat.ChatDTO(
                    member.getChat().getId(),
                    member.getChat().getName()
            ));
        }

        if (member.getUser() != null) {
            response.setUser(convertUserToDTO(member.getUser()));
        }

        return response;
    }

    private org.example.dto.User.UserDTO convertUserToDTO(org.example.entity.User user) {
        org.example.dto.User.UserDTO dto = new org.example.dto.User.UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}