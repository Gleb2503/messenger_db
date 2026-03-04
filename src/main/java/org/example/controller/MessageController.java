package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Message.MessageResponse;
import org.example.entity.Message;
import org.example.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Messages", description = "Управление сообщениями")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/chat/{chatId}/recent")
    @Operation(summary = "Получить последние сообщения чата")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список сообщений"),
            @ApiResponse(responseCode = "404", description = "Чат не найден")
    })
    public ResponseEntity<List<MessageResponse>> getRecentMessages(
            @Parameter(description = "ID чата", required = true, example = "1")
            @PathVariable Long chatId,

            @Parameter(description = "Количество сообщений (макс. 500)", example = "500")
            @RequestParam(defaultValue = "500") int limit) {

        if (limit > 500) {
            limit = 500;
        }

        List<Message> messages = messageService.getRecentMessagesByChatId(chatId, limit);

        List<MessageResponse> response = messages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chats/last")
    @Operation(summary = "Получить последнее сообщение для каждого чата")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список последних сообщений")
    })
    public ResponseEntity<List<MessageResponse>> getLastMessagesPerChat() {
        List<Message> messages = messageService.getLastMessagesPerChat();

        List<MessageResponse> response = messages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить сообщение по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сообщение найдено"),
            @ApiResponse(responseCode = "404", description = "Сообщение не найдено")
    })
    public ResponseEntity<MessageResponse> getMessageById(
            @Parameter(description = "ID сообщения", required = true, example = "1")
            @PathVariable Long id) {
        Message message = messageService.getMessageById(id);
        return ResponseEntity.ok(convertToResponse(message));
    }

    @GetMapping("/chat/{chatId}")
    @Operation(summary = "Получить сообщения чата")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<MessageResponse>> getMessagesByChat(
            @Parameter(description = "ID чата", required = true, example = "1")
            @PathVariable Long chatId) {
        List<Message> messages = messageService.getMessagesByChatId(chatId);
        List<MessageResponse> response = messages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sender/{senderId}")
    @Operation(summary = "Получить сообщения от пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<MessageResponse>> getMessagesBySender(
            @Parameter(description = "ID отправителя", required = true, example = "1")
            @PathVariable Long senderId) {
        List<Message> messages = messageService.getMessagesBySenderId(senderId);
        List<MessageResponse> response = messages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
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
            @RequestBody org.example.dto.Message.CreateMessageRequest request) {
        Message message = messageService.sendMessage(request.toEntity());
        return ResponseEntity.status(201).body(convertToResponse(message));
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
            @RequestBody org.example.dto.Message.CreateMessageRequest request) {
        Message message = messageService.updateMessage(id, request.toEntity());
        return ResponseEntity.ok(convertToResponse(message));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Сообщение удалено"),
            @ApiResponse(responseCode = "404", description = "Сообщение не найдено")
    })
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "ID сообщения", required = true, example = "1")
            @PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    private MessageResponse convertToResponse(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setContent(message.getContent());
        response.setMessageType(message.getMessageType());
        response.setIsEdited(message.getIsEdited());
        response.setIsDeleted(message.getIsDeleted());
        response.setDeliveryStatus(message.getDeliveryStatus());
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());

        if (message.getChat() != null) {
            response.setChat(new org.example.dto.Chat.ChatDTO(
                    message.getChat().getId(),
                    message.getChat().getName()
            ));
        }

        if (message.getSender() != null) {
            response.setSender(convertUserToDTO(message.getSender()));
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