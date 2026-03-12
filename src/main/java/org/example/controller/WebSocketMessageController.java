package org.example.controller;

import org.example.dto.Message.MessageResponse;
import org.example.dto.Message.CreateMessageRequest;
import org.example.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketMessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(
            @Payload CreateMessageRequest request,
            SimpMessageHeaderAccessor headerAccessor) {

        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        if (userId == null) {
            log.error("User not authenticated in WebSocket");
            return;
        }

        request.setSenderId(userId);

        log.info("WebSocket message received: chatId={}, userId={}, content={}",
                request.getChatId(), userId, request.getContent());

        MessageResponse response = messageService.sendMessage(request);

        log.info("Message saved, sending to topic: /topic/chat/{}", request.getChatId());

        messagingTemplate.convertAndSend(
                "/topic/chat/" + request.getChatId(),
                response
        );

        log.info("Message sent to /topic/chat/{}", request.getChatId());
    }
}