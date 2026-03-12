package org.example.service;

import org.example.dto.Chat.ChatResponse;
import org.example.dto.Chat.CreateChatRequest;
import org.example.dto.User.UserDTO;
import org.example.entity.Chat;
import org.example.entity.User;
import org.example.exeption.ResourceNotFoundException;
import org.example.repository.ChatRepository;
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
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public List<ChatResponse> getLast100Chats() {
        return chatRepository.findTop100ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ChatResponse getChatById(Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + id));
        return convertToResponse(chat);
    }

    public List<ChatResponse> getLast100ChatsByCreator(Long createdById) {
        return chatRepository.findTop100ByCreatedBy_IdOrderByCreatedAtDesc(createdById)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatResponse createChat(CreateChatRequest request, Long currentUserId) {
        Chat chat = request.toEntity();

        User creator = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Creator not found with id: " + currentUserId));
        chat.setCreatedBy(creator);

        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());

        Chat saved = chatRepository.save(chat);
        return convertToResponse(saved);
    }

    @Transactional
    public ChatResponse updateChat(Long id, CreateChatRequest request) {
        Chat existing = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + id));
        existing.setName(request.getName());
        if (request.getType() != null) {
            existing.setType(org.example.enums.ChatType.valueOf(request.getType()));
        }
        existing.setAvatarUrl(request.getAvatarUrl());
        existing.setUpdatedAt(LocalDateTime.now());
        Chat updated = chatRepository.save(existing);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteChat(Long id) {
        if (!chatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Chat not found with id: " + id);
        }
        chatRepository.deleteById(id);
    }

    private ChatResponse convertToResponse(Chat chat) {
        ChatResponse response = new ChatResponse();
        response.setId(chat.getId());
        response.setName(chat.getName());
        response.setType(chat.getType());
        response.setAvatarUrl(chat.getAvatarUrl());
        response.setCreatedAt(chat.getCreatedAt());
        response.setUpdatedAt(chat.getUpdatedAt());
        response.setLastMessageTime(chat.getLastMessageTime());

        if (chat.getCreatedBy() != null) {
            response.setCreatedBy(new UserDTO(
                    chat.getCreatedBy().getId(),
                    chat.getCreatedBy().getUsername(),
                    chat.getCreatedBy().getEmail()
            ));
        }

        return response;
    }
}