package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.Chat;
import org.example.service.ChatService;
import org.example.exeption.ResourceNotFoundException;
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
    @Operation(summary = "Получить все чаты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class),
                            examples = @ExampleObject(value = """
                    [
                      {
                        "id": 1,
                        "name": "Рабочий чат команды",
                        "type": "group",
                        "createdBy": {"id": 1, "username": "ivan_dev"},
                        "createdAt": "2026-02-27T17:10:00",
                        "lastMessageTime": "2026-02-27T17:30:00"
                      }
                    ]
                    """))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<Chat>> getAllChats() {
        return ResponseEntity.ok(chatService.getAllChats());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить чат по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Чат найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class))),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    public ResponseEntity<Chat> getChatById(
            @Parameter(description = "Уникальный идентификатор чата", required = true, example = "1")
            @PathVariable Long id) {
        return chatService.getChatById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + id));
    }

    @GetMapping("/created-by/{userId}")
    @Operation(summary = "Получить чаты созданные пользователем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<Chat>> getChatsByCreatedBy(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(chatService.getChatsByCreatedBy(userId));
    }

    @PostMapping
    @Operation(summary = "Создать новый чат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Чат успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "name": "Рабочий чат команды",
                      "type": "group",
                      "createdBy": {"id": 1, "username": "ivan_dev"},
                      "createdAt": "2026-02-27T17:10:00",
                      "lastMessageTime": null
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Chat> createChat(
            @Parameter(description = "Данные для создания чата", required = true)
            @RequestBody Chat chat) {
        return ResponseEntity.ok(chatService.createChat(chat));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить чат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Чат успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    public ResponseEntity<Chat> updateChat(
            @Parameter(description = "Уникальный идентификатор чата", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Обновленные данные чата", required = true)
            @RequestBody Chat chat) {
        return ResponseEntity.ok(chatService.updateChat(id, chat));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Чат успешно удален"),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    public ResponseEntity<Void> deleteChat(
            @Parameter(description = "Уникальный идентификатор чата", required = true, example = "1")
            @PathVariable Long id) {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }
}