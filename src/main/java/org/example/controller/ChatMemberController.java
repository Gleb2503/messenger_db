package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.ChatMember;
import org.example.service.ChatMemberService;
import org.example.exeption.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-members")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Chat Members", description = "Управление участниками чатов")
public class ChatMemberController {

    private final ChatMemberService chatMemberService;

    @GetMapping
    @Operation(summary = "Получить всех участников")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChatMember.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<ChatMember>> getAllMembers() {
        return ResponseEntity.ok(chatMemberService.getAllMembers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить участника по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Участник найден"),
            @ApiResponse(responseCode = "404", description = "Участник не найден")
    })
    public ResponseEntity<ChatMember> getMemberById(
            @Parameter(description = "Уникальный идентификатор участника", required = true, example = "1")
            @PathVariable Long id) {
        return chatMemberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить чаты пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<ChatMember>> getMembersByUserId(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(chatMemberService.getMembersByUserId(userId));
    }

    @GetMapping("/chat/{chatId}")
    @Operation(summary = "Получить участники чата")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    [
                      {
                        "id": 1,
                        "chat": {"id": 2, "name": "Рабочий чат"},
                        "user": {"id": 1, "username": "ivan_dev"},
                        "role": "owner",
                        "isActive": true,
                        "joinedAt": "2026-02-27T17:10:00"
                      },
                      {
                        "id": 2,
                        "chat": {"id": 2, "name": "Рабочий чат"},
                        "user": {"id": 2, "username": "maria_design"},
                        "role": "admin",
                        "isActive": true,
                        "joinedAt": "2026-02-27T17:15:00"
                      },
                      {
                        "id": 3,
                        "chat": {"id": 2, "name": "Рабочий чат"},
                        "user": {"id": 5, "username": "new_member"},
                        "role": "member",
                        "isActive": true,
                        "joinedAt": "2026-02-27T17:45:00"
                      }
                    ]
                    """))),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    public ResponseEntity<List<ChatMember>> getMembersByChatId(
            @Parameter(description = "Уникальный идентификатор чата", required = true, example = "2")
            @PathVariable Long chatId) {
        return ResponseEntity.ok(chatMemberService.getMembersByChatId(chatId));
    }

    @GetMapping("/chat/{chatId}/user/{userId}")
    @Operation(summary = "Проверить участника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Участник найден"),
            @ApiResponse(responseCode = "404", description = "Участник не найден")
    })
    public ResponseEntity<ChatMember> getMemberByChatAndUser(
            @Parameter(description = "Уникальный идентификатор чата", required = true, example = "2")
            @PathVariable Long chatId,
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return chatMemberService.getMemberByChatAndUser(chatId, userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
    }

    @PostMapping
    @Operation(summary = "Добавить участника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Участник успешно добавлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChatMember.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "chat": {"id": 2, "name": "Рабочий чат"},
                      "user": {"id": 5, "username": "new_member"},
                      "role": "member",
                      "isActive": true,
                      "joinedAt": "2026-02-27T17:45:00"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Чат или пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ChatMember> addMember(
            @Parameter(description = "Данные для создания участника", required = true)
            @RequestBody ChatMember chatMember) {
        return ResponseEntity.ok(chatMemberService.addMember(chatMember));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить участника")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Участник успешно удален"),
            @ApiResponse(responseCode = "404", description = "Участник не найден")
    })
    public ResponseEntity<Void> removeMember(
            @Parameter(description = "Уникальный идентификатор участника", required = true, example = "1")
            @PathVariable Long id) {
        chatMemberService.removeMember(id);
        return ResponseEntity.noContent().build();
    }
}
