package org.example.service;

import org.example.entity.Message;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.repository.MessageRepository;
import org.example.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Page<Message> getMessagesByChatId(Long chatId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return messageRepository.findByChat_IdOrderByCreatedAtDesc(chatId, pageable);
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public List<Message> getMessagesBySenderId(Long senderId) {
        return messageRepository.findBySender_Id(senderId);
    }

    @Transactional
    public Message sendMessage(Message message) {
        Message savedMessage = messageRepository.save(message);
        Chat chat = savedMessage.getChat();
        chat.setLastMessage(savedMessage);
        chat.setLastMessageTime(savedMessage.getCreatedAt());
        chatRepository.save(chat);
        return savedMessage;
    }

    @Transactional
    public Message editMessage(Long id, String content) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setContent(content);
        message.setIsEdited(true);
        return messageRepository.save(message);
    }

    @Transactional
    public Message deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setIsDeleted(true);
        message.setContent("This message was deleted");
        return messageRepository.save(message);
    }

    @Transactional
    public void hardDeleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}