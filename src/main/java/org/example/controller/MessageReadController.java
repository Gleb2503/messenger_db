package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.MessageRead;
import org.example.service.MessageReadService;
import org.example.exeption.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message-reads")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Message Reads", description = "Статусы прочтения сообщений")
public class MessageReadController {

    private final MessageReadService messageReadService;

    @GetMapping
    @Operation(summary = "Получить все статусы прочтения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageRead.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<MessageRead>> getAllReads() {
        return ResponseEntity.ok(messageReadService.getAllReads());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить статус по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус найден"),
            @ApiResponse(responseCode = "404", description = "Статус не найден")
    })
    public ResponseEntity<MessageRead> getReadById(
            @Parameter(description = "Уникальный идентификатор статуса", required = true, example = "1")
            @PathVariable Long id) {
        return messageReadService.getReadById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Read status not found with id: " + id));
    }

    @GetMapping("/message/{messageId}")
    @Operation(summary = "Получить кто прочитал сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    [
                      {
                        "id": 1,
                        "message": {"id": 1},
                        "user": {"id": 2, "username": "maria_design"},
                        "readAt": "2026-02-27T17:40:00"
                      },
                      {
                        "id": 2,
                        "message": {"id": 1},
                        "user": {"id": 3, "username": "petr_dev"},
                        "readAt": "2026-02-27T17:42:00"
                      }
                    ]
                    """))),
            @ApiResponse(responseCode = "404", description = "Сообщение не найдено")
    })
    public ResponseEntity<List<MessageRead>> getReadsByMessageId(
            @Parameter(description = "Уникальный идентификатор сообщения", required = true, example = "1")
            @PathVariable Long messageId) {
        return ResponseEntity.ok(messageReadService.getReadsByMessageId(messageId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить прочитанные пользователем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<MessageRead>> getReadsByUserId(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(messageReadService.getReadsByUserId(userId));
    }

    @PostMapping("/mark")
    @Operation(summary = "Отметить сообщение как прочитанное")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Статус успешно обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageRead.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "message": {"id": 1},
                      "user": {"id": 2},
                      "readAt": "2026-02-27T17:40:00"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Сообщение или пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<MessageRead> markAsRead(
            @Parameter(description = "Данные для отметки прочтения", required = true)
            @RequestBody MessageRead messageRead) {
        return ResponseEntity.ok(messageReadService.markAsRead(messageRead.getMessage().getId(), messageRead.getUser().getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить статус прочтения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Статус успешно удален"),
            @ApiResponse(responseCode = "404", description = "Статус не найден")
    })
    public ResponseEntity<Void> deleteRead(
            @Parameter(description = "Уникальный идентификатор статуса", required = true, example = "1")
            @PathVariable Long id) {
        messageReadService.deleteRead(id);
        return ResponseEntity.noContent().build();
    }
}