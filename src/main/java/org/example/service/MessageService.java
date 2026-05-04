package org.example.service;

import org.example.dto.Message.MessageResponse;
import org.example.dto.Message.CreateMessageRequest;
import org.example.dto.Chat.ChatDTO;
import org.example.dto.User.UserDTO;
import org.example.dto.Message.ReplyToDTO;
import org.example.dto.Attachment.AttachmentResponse;
import org.example.entity.Message;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.entity.MessageRead;
import org.example.entity.Attachment;
import org.example.enums.MessageType;
import org.example.enums.DeliveryStatus;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.MessageRepository;
import org.example.repository.ChatRepository;
import org.example.repository.UserRepository;
import org.example.repository.MessageReadsRepository;
import org.example.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
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
    private final MessageReadsRepository messageReadsRepository;
    private final AttachmentRepository attachmentRepository;

    public List<MessageResponse> getLast100MessagesByChat(Long chatId) {
        List<Message> messages = messageRepository.findTop100ByChatIdOrderByCreatedAtDesc(chatId);
        Collections.reverse(messages);
        return messages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public MessageResponse getMessageById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        return convertToResponse(message);
    }

    public List<MessageResponse> getLast100MessagesBySender(Long senderId) {
        return messageRepository.findTop100BySenderIdOrderByCreatedAtDesc(senderId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getLast100Messages() {
        return messageRepository.findTop100ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageResponse sendMessage(CreateMessageRequest request) {
        Message message = request.toEntity();

        if (message.getChat() != null && message.getChat().getId() != null) {
            Chat chat = chatRepository.findById(message.getChat().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + message.getChat().getId()));
            message.setChat(chat);
        } else {
            throw new ResourceNotFoundException("Chat ID is required");
        }

        if (message.getSender() != null && message.getSender().getId() != null) {
            User sender = userRepository.findById(message.getSender().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sender not found with id: " + message.getSender().getId()));
            message.setSender(sender);
        } else {
            throw new ResourceNotFoundException("Sender ID is required");
        }

        if (message.getReplyTo() != null && message.getReplyTo().getId() != null) {
            messageRepository.findById(message.getReplyTo().getId()).ifPresent(replyTo -> {
                if (replyTo.getChat().getId().equals(message.getChat().getId())) {
                    message.setReplyTo(replyTo);
                } else {
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

        updateChatLastMessageTime(saved.getChat(), saved.getCreatedAt());

        return convertToResponse(saved);
    }

    @Transactional
    public MessageResponse updateMessage(Long id, CreateMessageRequest request) {
        Message existing = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));

        existing.setContent(request.getContent());

        if (request.getMessageType() != null) {
            existing.setMessageType(MessageType.valueOf(request.getMessageType()));
        }

        existing.setIsEdited(true);
        existing.setUpdatedAt(LocalDateTime.now());

        Message updated = messageRepository.save(existing);

        Chat chat = updated.getChat();
        if (chat != null) {
            messageRepository.findTopByChatIdOrderByCreatedAtDesc(chat.getId())
                    .ifPresent(lastMsg -> {
                        if (lastMsg.getId().equals(updated.getId())) {
                            updateChatLastMessageTime(chat, updated.getUpdatedAt());
                        }
                    });
        }

        return convertToResponse(updated);
    }

    @Transactional
    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
        Chat chat = message.getChat();

        messageRepository.deleteById(id);

        if (chat != null) {
            messageRepository.findTopByChatIdOrderByCreatedAtDesc(chat.getId())
                    .ifPresent(lastMsg -> updateChatLastMessageTime(chat, lastMsg.getCreatedAt()));
        }
    }

    private void updateChatLastMessageTime(Chat chat, LocalDateTime time) {
        if (chat != null && time != null) {
            chat.setLastMessageTime(time);
            chatRepository.save(chat);
            chatRepository.flush();
        }
    }

    @Transactional
    public boolean markMessageAsRead(Long messageId, Long userId) {
        log.info("Marking message {} as read by user {}", messageId, userId);

        if (messageReadsRepository.existsByUserIdAndMessageId(userId, messageId)) {
            log.debug("Message {} already read by user {}", messageId, userId);
            return false;
        }

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found: " + messageId));

        if (message.getDeliveryStatus() != DeliveryStatus.read) {
            message.setDeliveryStatus(DeliveryStatus.read);
            message.setUpdatedAt(LocalDateTime.now());
            messageRepository.save(message);
        }

        MessageRead messageRead = new MessageRead();
        messageRead.setMessage(message);
        messageRead.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId)));
        messageRead.setReadAt(LocalDateTime.now());

        messageReadsRepository.save(messageRead);

        log.info("Saved to message_reads: messageId={}, userId={}", messageId, userId);
        return true;
    }
    public List<MessageResponse> getMessagesPaginated(Long chatId, Long beforeId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt", "id"));

        List<Message> messages;
        if (beforeId != null) {
            messages = messageRepository.findMessagesBefore(chatId, beforeId, pageRequest);
        } else {
            messages = messageRepository.findFirstPageByChatId(chatId, pageRequest);
        }


        Collections.reverse(messages);

        return messages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private MessageResponse convertToResponse(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setContent(message.getContent());
        response.setMessageType(message.getMessageType());
        response.setIsEdited(message.getIsEdited() != null ? message.getIsEdited() : false);
        response.setIsDeleted(message.getIsDeleted() != null ? message.getIsDeleted() : false);
        response.setDeliveryStatus(message.getDeliveryStatus() != null ? message.getDeliveryStatus() : DeliveryStatus.sent);
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());

        if (message.getChat() != null) {
            response.setChat(new ChatDTO(message.getChat().getId(), message.getChat().getName()));
        }

        if (message.getSender() != null) {
            response.setSender(new UserDTO(
                    message.getSender().getId(),
                    message.getSender().getUsername(),
                    message.getSender().getEmail(),
                    message.getSender().getAvatarUrl()
            ));
        }

        if (message.getReplyTo() != null && message.getReplyTo().getSender() != null) {
            UserDTO replySender = new UserDTO(
                    message.getReplyTo().getSender().getId(),
                    message.getReplyTo().getSender().getUsername(),
                    message.getReplyTo().getSender().getEmail(),
                    message.getReplyTo().getSender().getAvatarUrl()
            );
            response.setReplyTo(new ReplyToDTO(
                    message.getReplyTo().getId(),
                    message.getReplyTo().getContent(),
                    replySender
            ));
        }

        List<Attachment> attachments = attachmentRepository.findByMessageId(message.getId());
        if (!attachments.isEmpty()) {
            response.setAttachments(
                    attachments.stream()
                            .map(this::convertAttachmentToResponse)
                            .collect(Collectors.toList())
            );
        }

        return response;
    }

    private AttachmentResponse convertAttachmentToResponse(Attachment attachment) {
        AttachmentResponse response = new AttachmentResponse();
        response.setId(attachment.getId());
        response.setFileUrl(attachment.getFileUrl());
        response.setFileName(attachment.getFileName());
        response.setFileSize(attachment.getFileSize());
        response.setFileType(attachment.getFileType());
        response.setThumbnailUrl(attachment.getThumbnailUrl());
        response.setCreatedAt(attachment.getCreatedAt());
        return response;
    }
}