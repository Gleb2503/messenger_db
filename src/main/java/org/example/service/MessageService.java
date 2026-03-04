package org.example.service;

import org.example.entity.Message;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.MessageRepository;
import org.example.repository.ChatRepository;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public List<Message> getRecentMessagesByChatId(Long chatId, int limit) {
        if (!chatRepository.existsById(chatId)) {
            throw new ResourceNotFoundException("Chat not found with id: " + chatId);
        }
        var pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return messageRepository.findByChatIdOrderByCreatedAtDesc(chatId, pageable);
    }

    public List<Message> getLastMessagesPerChat() {
        return messageRepository.findLastMessagePerChat();
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
    }

    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);
    }

    public List<Message> getMessagesBySenderId(Long senderId) {
        return messageRepository.findBySenderId(senderId);
    }

    @Transactional
    public Message sendMessage(Message message) {
        if (message.getChat() != null && message.getChat().getId() != null) {
            var chat = chatRepository.findById(message.getChat().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + message.getChat().getId()));
            message.setChat(chat);
        }

        if (message.getSender() != null && message.getSender().getId() != null) {
            var sender = userRepository.findById(message.getSender().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sender not found with id: " + message.getSender().getId()));
            message.setSender(sender);
        }

        if (message.getReplyTo() != null && message.getReplyTo().getId() != null) {
            messageRepository.findById(message.getReplyTo().getId()).ifPresent(replyTo -> {
                if (replyTo.getChat() != null && replyTo.getChat().getId() != null &&
                        message.getChat() != null && message.getChat().getId() != null &&
                        replyTo.getChat().getId().equals(message.getChat().getId())) {
                    message.setReplyTo(replyTo);
                }
            });
        }

        message.setIsEdited(false);
        message.setIsDeleted(false);
        message.setCreatedAt(java.time.LocalDateTime.now());
        message.setUpdatedAt(java.time.LocalDateTime.now());

        return messageRepository.save(message);
    }

    @Transactional
    public Message updateMessage(Long id, Message message) {
        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));

        existingMessage.setContent(message.getContent());
        existingMessage.setMessageType(message.getMessageType());
        existingMessage.setIsEdited(true);
        existingMessage.setUpdatedAt(java.time.LocalDateTime.now());

        return messageRepository.save(existingMessage);
    }

    @Transactional
    public void deleteMessage(Long id) {
        if (!messageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Message not found with id: " + id);
        }
        messageRepository.deleteById(id);
    }
}