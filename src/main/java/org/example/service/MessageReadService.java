package org.example.service;

import org.example.entity.Message;
import org.example.entity.MessageRead;
import org.example.entity.User;
import org.example.repository.MessageReadsRepository;
import org.example.repository.MessageRepository;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageReadService {

    private final MessageReadsRepository messageReadRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<MessageRead> getAllReads() {
        return messageReadRepository.findAll();
    }

    public List<MessageRead> getReadsByMessageId(Long messageId) {
        return messageReadRepository.findByMessage_Id(messageId);
    }

    public List<MessageRead> getReadsByUserId(Long userId) {
        return messageReadRepository.findByUser_Id(userId);
    }

    public Optional<MessageRead> getReadById(Long id) {
        return messageReadRepository.findById(id);
    }

    public Optional<MessageRead> getReadByMessageAndUser(Long messageId, Long userId) {
        return messageReadRepository.findByMessage_IdAndUser_Id(messageId, userId);
    }

    @Transactional
    public MessageRead markAsRead(Long messageId, Long userId) {
        return messageReadRepository.findByMessage_IdAndUser_Id(messageId, userId)
                .orElseGet(() -> {
                    Message message = messageRepository.findById(messageId)
                            .orElseThrow(() -> new RuntimeException("Message not found"));
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    MessageRead messageRead = new MessageRead();
                    messageRead.setMessage(message);
                    messageRead.setUser(user);
                    messageRead.setReadAt(LocalDateTime.now());
                    return messageReadRepository.save(messageRead);
                });
    }

    @Transactional
    public void deleteRead(Long id) {
        messageReadRepository.deleteById(id);
    }
}