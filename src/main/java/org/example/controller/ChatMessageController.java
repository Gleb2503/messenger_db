package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.Message.MessageResponse;
import org.example.dto.Message.CreateMessageRequest;
import org.example.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatMessageController {

    private final MessageService messageService;

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessagesByChat(@PathVariable Long chatId) {
        List<MessageResponse> messages = messageService.getLast100MessagesByChat(chatId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<MessageResponse> sendMessageToChat(
            @PathVariable Long chatId,
            @RequestBody CreateMessageRequest request,
            HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        Long userId = extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        request.setSenderId(userId);
        request.setChatId(chatId);

        MessageResponse response = messageService.sendMessage(request);
        return ResponseEntity.status(201).body(response);
    }

    private Long extractUserId(String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            String[] parts = jwt.split("\\.");
            if (parts.length >= 2) {
                String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
                com.fasterxml.jackson.databind.JsonNode node =
                        new com.fasterxml.jackson.databind.ObjectMapper().readTree(payload);
                return node.has("userId") ? node.get("userId").asLong() : null;
            }
        } catch (Exception e) {
        }
        return null;
    }
}