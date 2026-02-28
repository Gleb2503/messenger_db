package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.Message;
import org.example.service.MessageService;
import org.example.exeption.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Messages", description = "Управление сообщениями")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    @Operation(summary = "Получить все сообщения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Message.class),
                            examples = @ExampleObject(value = """
                    [
                      {
                        "id": 1,
                        "chat": {"id": 1, "name": "Рабочий чат"},
                        "sender": {"id": 1, "username": "ivan_dev"},
                        "content": "Привет всем!",
                        "messageType": "text",
                        "deliveryStatus": "sent",
                        "createdAt": "2026-02-27T17:30:00"
                      }
                    ]
                    """))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить сообщение по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сообщение найдено"),
            @ApiResponse(responseCode = "404", description = "Сообщение не найдено")
    })
    public ResponseEntity<Message> getMessageById(
            @Parameter(description = "Уникальный идентификатор сообщения", required = true, example = "1")
            @PathVariable Long id) {
        return messageService.getMessageById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
    }

    @GetMapping("/chat/{chatId}")
    @Operation(summary = "Получить сообщения чата с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    {
                      "content": [
                        {
                          "id": 1,
                          "sender": {"id": 1, "username": "ivan_dev"},
                          "content": "Привет всем!",
                          "messageType": "text",
                          "createdAt": "2026-02-27T17:30:00"
                        }
                      ],
                      "totalPages": 1,
                      "totalElements": 1,
                      "size": 20,
                      "number": 0
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    public ResponseEntity<Page<Message>> getMessagesByChatId(
            @Parameter(description = "Уникальный идентификатор чата", required = true, example = "1")
            @PathVariable Long chatId,
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(messageService.getMessagesByChatId(chatId, page, size));
    }

    @GetMapping("/sender/{senderId}")
    @Operation(summary = "Получить сообщения от пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<Message>> getMessagesBySenderId(
            @Parameter(description = "Уникальный идентификатор отправителя", required = true, example = "1")
            @PathVariable Long senderId) {
        return ResponseEntity.ok(messageService.getMessagesBySenderId(senderId));
    }

    @PostMapping
    @Operation(summary = "Отправить новое сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Сообщение успешно отправлено",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Message.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "chat": {"id": 1, "name": "Рабочий чат"},
                      "sender": {"id": 1, "username": "ivan_dev"},
                      "content": "Привет всем! Как дела?",
                      "messageType": "text",
                      "deliveryStatus": "sent",
                      "createdAt": "2026-02-27T17:30:00"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Чат или пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Message> sendMessage(
            @Parameter(description = "Данные для создания сообщения", required = true)
            @RequestBody Message message) {
        return ResponseEntity.ok(messageService.sendMessage(message));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сообщение успешно обновлено",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "content": "Привет всем! Как дела? 👋",
                      "isEdited": true,
                      "updatedAt": "2026-02-27T17:35:00"
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "Сообщение не найдено"),
            @ApiResponse(responseCode = "403", description = "Нет прав на редактирование")
    })
    public ResponseEntity<Message> editMessage(
            @Parameter(description = "Уникальный идентификатор сообщения", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Обновленные данные сообщения", required = true)
            @RequestBody Message message) {
        return ResponseEntity.ok(messageService.editMessage(id, message.getContent()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сообщение успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Сообщение не найдено"),
            @ApiResponse(responseCode = "403", description = "Нет прав на удаление")
    })
    public ResponseEntity<Message> deleteMessage(
            @Parameter(description = "Уникальный идентификатор сообщения", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(messageService.deleteMessage(id));
    }
}