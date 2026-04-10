package org.example.controller;

import org.example.dto.Message.MessageResponse;
import org.example.dto.Message.CreateMessageRequest;
import org.example.dto.MessageRead.CreateMessageReadRequest;
import org.example.dto.MessageRead.MessageStatusUpdate;
import org.example.dto.UserStatus.UserStatusRequest;
import org.example.service.MessageService;
import org.example.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import jakarta.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketMessageController {

    private final MessageService messageService;
    private final UserStatusService userStatusService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    public void init() {
        log.info("WebSocketMessageController INITIALIZED");
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(
            @Payload CreateMessageRequest request,
            SimpMessageHeaderAccessor headerAccessor) {

        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        if (userId == null) {
            log.error("User not authenticated in WebSocket");
            return;
        }

        log.info("Received /app/chat.sendMessage: chatId={}, userId={}", request.getChatId(), userId);

        request.setSenderId(userId);
        MessageResponse response = messageService.sendMessage(request);
        messagingTemplate.convertAndSend("/topic/chat/" + request.getChatId(), response);
    }

    @MessageMapping("/user.status")
    public void handleUserStatus(@Payload UserStatusRequest request) {
        log.info("Received /app/user.status: request={}", request);

        if (request == null) {
            log.warn("UserStatusRequest is null");
            return;
        }

        log.info("userId={}, online={}, chatId={}", request.getUserId(), request.getOnline(), request.getChatId());

        if (request.getUserId() != null && request.getOnline() != null) {
            log.info("User {} status: {}", request.getUserId(), request.getOnline() ? "online" : "offline");
            userStatusService.updateStatus(request.getUserId(), request.getOnline());
        } else {
            log.warn("Missing userId or online in request: {}", request);
        }
    }

    @MessageMapping("/message.read")
    public void handleMessageRead(@Payload CreateMessageReadRequest request) {
        log.info("Received /app/message.read: request={}", request);

        if (request == null) {
            log.warn("CreateMessageReadRequest is null");
            return;
        }

        log.info("messageId={}, userId={}, chatId={}", request.getMessageId(), request.getUserId(), request.getChatId());

        if (request.getMessageId() != null && request.getUserId() != null) {
            log.info("Message {} marked as read by user {}", request.getMessageId(), request.getUserId());

            boolean updated = messageService.markMessageAsRead(request.getMessageId(), request.getUserId());

            if (updated) {
                MessageStatusUpdate update = new MessageStatusUpdate(
                        request.getMessageId(),
                        "read",
                        request.getChatId(),
                        System.currentTimeMillis()
                );

                log.info("Sending status update to /topic/message/status: messageId={}", request.getMessageId());
                messagingTemplate.convertAndSend("/topic/message/status", update);
            } else {
                log.warn("Message {} not updated (maybe already read or not found)", request.getMessageId());
            }
        } else {
            log.warn("Missing messageId or userId in request: {}", request);
        }
    }

    @MessageMapping("/debug/**")
    public void debugStompMessages(@Payload(required = false) Object payload, Message<?> message) {
        log.debug("DEBUG: dest={}, payload={}", message.getHeaders().get("destination"), payload);
    }
}