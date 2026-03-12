package org.example.service;

import org.example.dto.MessageRead.MessageReadResponse;
import org.example.dto.MessageRead.CreateMessageReadRequest;
import org.example.dto.Message.MessageDTO;
import org.example.dto.User.UserDTO;
import org.example.entity.MessageRead;
import org.example.entity.Message;
import org.example.entity.User;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.MessageReadsRepository;
import org.example.repository.MessageRepository;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageReadService {

    private final MessageReadsRepository messageReadRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<MessageReadResponse> getLast100ReadsByUser(Long userId) {
        return messageReadRepository.findTop100ByUserIdOrderByReadAtDesc(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<MessageReadResponse> getLast100ReadsByMessage(Long messageId) {
        return messageReadRepository.findTop100ByMessageIdOrderByReadAtDesc(messageId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageReadResponse markAsRead(CreateMessageReadRequest request) {
        MessageRead messageRead = request.toEntity();

        if (messageRead.getMessage() != null && messageRead.getMessage().getId() != null) {
            Message message = messageRepository.findById(messageRead.getMessage().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageRead.getMessage().getId()));
            messageRead.setMessage(message);
        }

        if (messageRead.getUser() != null && messageRead.getUser().getId() != null) {
            User user = userRepository.findById(messageRead.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + messageRead.getUser().getId()));
            messageRead.setUser(user);
        }

        messageRead.setReadAt(LocalDateTime.now());
        MessageRead saved = messageReadRepository.save(messageRead);
        return convertToResponse(saved);
    }

    @Transactional
    public void deleteRead(Long id) {
        if (!messageReadRepository.existsById(id)) {
            throw new ResourceNotFoundException("MessageRead not found with id: " + id);
        }
        messageReadRepository.deleteById(id);
    }

    private MessageReadResponse convertToResponse(MessageRead read) {
        MessageReadResponse response = new MessageReadResponse();
        response.setId(read.getId());
        response.setReadAt(read.getReadAt());

        if (read.getMessage() != null) {
            response.setMessage(new MessageDTO(
                    read.getMessage().getId(),
                    read.getMessage().getContent()
            ));
        }

        if (read.getUser() != null) {
            response.setUser(new UserDTO(
                    read.getUser().getId(),
                    read.getUser().getUsername(),
                    read.getUser().getEmail()
            ));
        }

        return response;
    }
}