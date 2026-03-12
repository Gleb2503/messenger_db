package org.example.service;

import org.example.dto.Message.MessageResponse;
import org.example.dto.Message.CreateMessageRequest;
import org.example.dto.Chat.ChatDTO;
import org.example.dto.User.UserDTO;
import org.example.dto.Message.ReplyToDTO;
import org.example.entity.Message;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.enums.MessageType;
import org.example.enums.DeliveryStatus;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.MessageRepository;
import org.example.repository.ChatRepository;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public List<MessageResponse> getLast100Messages() {
        log.info("Fetching last 100 messages globally");
        return messageRepository.findTop100ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public MessageResponse getMessageById(Long id) {
        log.info("Fetching message by id: {}", id);
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        return convertToResponse(message);
    }

    public List<MessageResponse> getLast100MessagesByChat(Long chatId) {
        log.info("Fetching last 100 messages for chat: {}", chatId);
        return messageRepository.findTop100ByChatIdOrderByCreatedAtAsc(chatId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getLast100MessagesBySender(Long senderId) {
        log.info("Fetching last 100 messages for sender: {}", senderId);
        return messageRepository.findTop100BySenderIdOrderByCreatedAtDesc(senderId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageResponse sendMessage(CreateMessageRequest request) {
        log.info("Sending message: chatId={}, senderId={}, contentLength={}",
                request.getChatId(),
                request.getSenderId(),
                request.getContent() != null ? request.getContent().length() : 0);

        Message message = request.toEntity();

        if (message.getChat() != null && message.getChat().getId() != null) {
            Chat chat = chatRepository.findById(message.getChat().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Chat not found with id: " + message.getChat().getId()));
            message.setChat(chat);
            log.info("Chat found: id={}, name={}", chat.getId(), chat.getName());
        } else {
            log.error("Chat is null or chatId is null");
            throw new ResourceNotFoundException("Chat ID is required");
        }

        if (message.getSender() != null && message.getSender().getId() != null) {
            User sender = userRepository.findById(message.getSender().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Sender not found with id: " + message.getSender().getId()));
            message.setSender(sender);
            log.info("Sender found: id={}, username={}", sender.getId(), sender.getUsername());
        } else {
            log.error("Sender is null or senderId is null");
            throw new ResourceNotFoundException("Sender ID is required");
        }

        if (message.getReplyTo() != null && message.getReplyTo().getId() != null) {
            messageRepository.findById(message.getReplyTo().getId()).ifPresent(replyTo -> {
                if (replyTo.getChat().getId().equals(message.getChat().getId())) {
                    message.setReplyTo(replyTo);
                    log.info("ReplyTo set: id={}", replyTo.getId());
                } else {
                    log.warn("ReplyTo chat mismatch, ignoring replyTo");
                    message.setReplyTo(null);
                }
            });
        }

        message.setIsEdited(false);
        message.setIsDeleted(false);
        message.setDeliveryStatus(DeliveryStatus.sent);
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());

        Message saved = messageRepository.save(message);
        log.info("Message saved to DB: id={}, chatId={}, senderId={}",
                saved.getId(), saved.getChat().getId(), saved.getSender().getId());

        MessageResponse response = convertToResponse(saved);
        log.info("MessageResponse created: id={}, contentLength={}",
                response.getId(), response.getContent() != null ? response.getContent().length() : 0);

        return response;
    }

    @Transactional
    public MessageResponse updateMessage(Long id, CreateMessageRequest request) {
        log.info("Updating message: id={}", id);

        Message existing = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));

        existing.setContent(request.getContent());

        if (request.getMessageType() != null) {
            existing.setMessageType(MessageType.valueOf(request.getMessageType()));
        }

        existing.setIsEdited(true);
        existing.setUpdatedAt(LocalDateTime.now());

        Message updated = messageRepository.save(existing);
        log.info("Message updated: id={}, isEdited={}", updated.getId(), updated.getIsEdited());

        return convertToResponse(updated);
    }

    @Transactional
    public void deleteMessage(Long id) {
        log.info("Deleting message: id={}", id);

        if (!messageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Message not found with id: " + id);
        }

        messageRepository.deleteById(id);
        log.info("Message deleted: id={}", id);
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
            response.setChat(new ChatDTO(
                    message.getChat().getId(),
                    message.getChat().getName()
            ));
        }

        if (message.getSender() != null) {
            response.setSender(new UserDTO(
                    message.getSender().getId(),
                    message.getSender().getUsername(),
                    message.getSender().getEmail()
            ));
        }

        if (message.getReplyTo() != null) {
            UserDTO replySender = message.getReplyTo().getSender() != null
                    ? new UserDTO(
                    message.getReplyTo().getSender().getId(),
                    message.getReplyTo().getSender().getUsername(),
                    message.getReplyTo().getSender().getEmail()
            )
                    : null;
            response.setReplyTo(new ReplyToDTO(
                    message.getReplyTo().getId(),
                    message.getReplyTo().getContent(),
                    replySender
            ));
        }

        return response;
    }
}