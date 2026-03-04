package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.MessageRead.MessageReadResponse;
import org.example.dto.MessageRead.CreateMessageReadRequest;
import org.example.service.MessageReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message-reads")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "MessageReads", description = "Статусы прочтения сообщений")
public class MessageReadController {

    private final MessageReadService messageReadService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить последние 100 прочтений пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<MessageReadResponse>> getLast100ReadsByUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        List<MessageReadResponse> response = messageReadService.getLast100ReadsByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/message/{messageId}")
    @Operation(summary = "Получить последние 100 прочтений сообщения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<MessageReadResponse>> getLast100ReadsByMessage(
            @Parameter(description = "ID сообщения", required = true, example = "1")
            @PathVariable Long messageId) {
        List<MessageReadResponse> response = messageReadService.getLast100ReadsByMessage(messageId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Отметить сообщение как прочитанное")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Прочтение записано"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<MessageReadResponse> markAsRead(
            @Parameter(description = "Данные прочтения", required = true)
            @RequestBody CreateMessageReadRequest request) {
        MessageReadResponse response = messageReadService.markAsRead(request);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить запись о прочтении")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Запись удалена"),
            @ApiResponse(responseCode = "404", description = "Запись не найдена")
    })
    public ResponseEntity<Void> deleteRead(
            @Parameter(description = "ID записи", required = true, example = "1")
            @PathVariable Long id) {
        messageReadService.deleteRead(id);
        return ResponseEntity.noContent().build();
    }
}